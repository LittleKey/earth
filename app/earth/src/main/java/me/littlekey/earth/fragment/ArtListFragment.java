package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.network.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.activity.BaseActivity;
import me.littlekey.earth.dialog.CategoryDialog;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.widget.IconFontTextView;
import me.littlekey.earth.widget.SearchCompleteView;

/**
 * Created by littlekey on 16/6/11.
 */
public class ArtListFragment extends BaseFragment
    implements
      View.OnClickListener,
      TextView.OnEditorActionListener,
      TextWatcher {

  private ListFragment mContentFragment;
  private SearchCompleteView mSearchView;
  private IconFontTextView mBtnClear;

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
    mBtnClear = (IconFontTextView) view.findViewById(R.id.btn_clear);
    mBtnClear.setOnClickListener(this);
//    view.findViewById(R.id.fab).setOnClickListener(this);
    mSearchView = (SearchCompleteView) view.findViewById(R.id.search);
    mSearchView.setOnEditorActionListener(this);
    ArrayList<String> paths = getArguments().getStringArrayList(Const.KEY_API_PATH);
    if (!CollectionUtils.isEmpty(paths)) {
      mSearchView.setText(paths.get(paths.size() - 1));
      mBtnClear.setText(R.string.img_cross);
    } else {
      mBtnClear.setText(R.string.img_search);
    }
    mSearchView.addTextChangedListener(this);
  }

  @Override
  public void onDestroyView() {
    EarthApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroyView();
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    if (TextUtils.isEmpty(s.toString())) {
      mBtnClear.setText(R.string.img_search);
    } else {
      mBtnClear.setText(R.string.img_cross);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
//      case R.id.fab:
//        mContentFragment.smoothScrollToPosition(0);
//        break;
      case R.id.btn_clear:
        if (TextUtils.isEmpty(mSearchView.getText().toString())) {
          CategoryDialog.newInstance().show(getActivity());
        } else {
          mSearchView.setText(Const.EMPTY_STRING);
        }
        break;
    }
  }

  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
      search();
    }
    return false;
  }

  private void search() {
    List<NameValuePair> pairs = new ArrayList<>();
    String searchContent = mSearchView.getText().toString();
    pairs.add(new NameValuePair(Const.KEY_F_SEARCH, searchContent));
    for (Model.Category category: EarthApplication.getInstance().getSelectedCategory()) {
      pairs.add(new NameValuePair(category.getSearchName(), Const.ONE));
    }
    pairs.add(new NameValuePair(Const.KEY_F_APPLY, Const.APPLY_AND_FILTER));
    mContentFragment.resetApi(ApiType.SEARCH_LIST, null, pairs.toArray(new NameValuePair[pairs.size()]));
    EarthApplication.getInstance().addSearchHistory(searchContent);
    ((BaseActivity) getActivity()).closeKeyboard();
    mSearchView.dismissDropDown();
  }

  protected Fragment createContentFragment() {
    Bundle bundle = getArguments();
    ArrayList<String> paths = bundle.getStringArrayList(Const.KEY_API_PATH);
    // TODO : add to Const
    switch (CollectionUtils.isEmpty(paths) ? Const.API_ROOT : paths.get(0)) {
      case Const.TAG:
        bundle.putInt(Const.KEY_API_TYPE, ApiType.TAG_LIST.ordinal());
        bundle.putStringArrayList(Const.KEY_API_PATH, paths);
        break;
      case Const.API_ROOT:
        bundle.putInt(Const.KEY_API_TYPE, ApiType.HOME_LIST.ordinal());
        break;
    }
    return mContentFragment = ListFragment.newInstance(bundle);
  }
}
