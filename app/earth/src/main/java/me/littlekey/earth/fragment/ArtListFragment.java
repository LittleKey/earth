package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.network.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.dialog.CategoryDialog;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.utils.Const;

/**
 * Created by littlekey on 16/6/11.
 */
public class ArtListFragment extends BaseFragment
    implements
      View.OnClickListener,
      TextView.OnEditorActionListener {

  private ListFragment mContentFragment;
  private EditText mSearchView;

  public static ArtListFragment newInstance(Bundle bundle) {
    ArtListFragment fragment = new ArtListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_art_list, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    FragmentManager fm = getChildFragmentManager();
    Fragment contentFragment = fm.findFragmentById(R.id.fragment_container);
    if (contentFragment == null) {
      contentFragment = createContentFragment();
      fm.beginTransaction()
          .add(R.id.fragment_container, contentFragment)
          .commit();
    }
    view.findViewById(R.id.btn_search).setOnClickListener(this);
    view.findViewById(R.id.fab).setOnClickListener(this);
    mSearchView = (EditText) view.findViewById(R.id.search);
    mSearchView.setOnEditorActionListener(this);
  }

  @Override
  public void onDestroyView() {
    EarthApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroyView();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fab:
//        mContentFragment.smoothScrollToPosition(0);
        CategoryDialog.newInstance().show(getActivity());
        break;
      case R.id.btn_search:
        search();
        break;
    }
  }

  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if (2 <= actionId && actionId <= 6) {
      search();
    }
    return false;
  }

  private void search() {
    List<NameValuePair> pairs = new ArrayList<>();
    pairs.add(new NameValuePair(Const.KEY_F_SEARCH, mSearchView.getText().toString()));
    for (Model.Category category: EarthApplication.getInstance().getSelectedCategory()) {
      pairs.add(new NameValuePair(category.getSearchName(), Const.ONE));
    }
    pairs.add(new NameValuePair(Const.KEY_F_APPLY, Const.APPLY_AND_FILTER));
    mContentFragment.resetApi(ApiType.SEARCH_LIST, null, pairs.toArray(new NameValuePair[pairs.size()]));
  }

  protected Fragment createContentFragment() {
    Bundle bundle = getArguments();
    ArrayList<String> paths = bundle.getStringArrayList(Const.KEY_API_PATH);
    // TODO : add to Const
    switch (CollectionUtils.isEmpty(paths) ? "/" : paths.get(0)) {
      case "tag":
        bundle.putInt(Const.KEY_API_TYPE, ApiType.TAG_LIST.ordinal());
        bundle.putStringArrayList(Const.KEY_API_PATH, paths);
        break;
      case "/":
        bundle.putInt(Const.KEY_API_TYPE, ApiType.HOME_LIST.ordinal());
        break;
    }
    return mContentFragment = ListFragment.newInstance(bundle);
  }
}
