package me.littlekey.earth.fragment;

import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.network.NameValuePair;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.activity.ArtListActivity;
import me.littlekey.earth.activity.BaseActivity;
import me.littlekey.earth.activity.DownloadActivity;
import me.littlekey.earth.dialog.CategoryDialog;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;
import me.littlekey.earth.utils.NavigationManager;
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

  private DrawerLayout mDrawerLayout;
  private ListFragment mContentFragment;
  private SearchCompleteView mSearchView;
  private IconFontTextView mBtnClear;

  public static ArtListFragment newInstance(Bundle bundle) {
    ArtListFragment fragment = new ArtListFragment();
    ArrayList<String> paths = bundle.getStringArrayList(Const.KEY_API_PATH);
    switch (CollectionUtils.isEmpty(paths) ? Const.API_ROOT : paths.get(0)) {
      case Const.TAG:
        bundle.putInt(Const.KEY_API_TYPE, ApiType.TAG_LIST.ordinal());
        break;
      case Const.API_ROOT:
        bundle.putInt(Const.KEY_API_TYPE, ApiType.HOME_LIST.ordinal());
        break;
      case Const.FAV_LIST:
        bundle.putInt(Const.KEY_API_TYPE, ApiType.FAV_LIST.ordinal());
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new NameValuePair(Const.KEY_FAV_CAT, Const.ALL));
        bundle.putSerializable(Const.KEY_API_QUERY, pairs);
        break;
    }
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_art_list, container, false);
  }

  @Override
  @SuppressWarnings("RtlHardcoded")
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
    SimpleDraweeView cover = (SimpleDraweeView) view.findViewById(R.id.cover);
    cover.setImageURI(Uri.parse(EarthUtils.buildImage(R.mipmap.kiseki)));
    cover.getHierarchy().setActualImageFocusPoint(new PointF(0, 0));
    ListView navigationListView = (ListView) view.findViewById(R.id.list_view);
    NavigationAdapter navigationAdapter = new NavigationAdapter(this);
    navigationListView.setAdapter(navigationAdapter);
    navigationListView.setOnItemClickListener(navigationAdapter);
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
    switch (ApiType.values()[getArguments().getInt(Const.KEY_API_TYPE)]) {
      case TAG_LIST:
        if (!CollectionUtils.isEmpty(paths)) {
          mSearchView.setText(paths.get(paths.size() - 1));
          mBtnClear.setText(R.string.img_cross);
        }
        break;
      case HOME_LIST:
        mBtnClear.setText(R.string.img_search);
        break;
      case FAV_LIST:
        mSearchView.setHint(R.string.fav_list);
        mBtnClear.setText(R.string.img_search);
        break;
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
      case R.id.fab:
        break;
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
    ApiType searchApi;
    switch (ApiType.values()[getArguments().getInt(Const.KEY_API_TYPE)]) {
      case FAV_LIST:
        pairs.add(new NameValuePair(Const.KEY_FAV_CAT, Const.ALL));
        pairs.add(new NameValuePair(Const.KEY_F_APPLY, Const.SEARCH_AND_FAVORITES));
        searchApi = ApiType.SEARCH_FAV_LIST;
        break;
      case HOME_LIST:
        for (Model.Category category: EarthApplication.getInstance().getSelectedCategory()) {
          pairs.add(new NameValuePair(category.getSearchName(), Const.ONE));
        }
      case TAG_LIST:
      default:
        pairs.add(new NameValuePair(Const.KEY_F_APPLY, Const.APPLY_AND_FILTER));
        searchApi = ApiType.SEARCH_LIST;
        break;
    }
    pairs.add(new NameValuePair(Const.KEY_F_SEARCH, searchContent));
    mContentFragment.resetApi(searchApi, null, pairs.toArray(new NameValuePair[pairs.size()]));
    EarthApplication.getInstance().addSearchHistory(searchContent);
    ((BaseActivity) getActivity()).closeKeyboard();
    mSearchView.dismissDropDown();
  }

  protected Fragment createContentFragment() {
    return mContentFragment = ListFragment.newInstance(getArguments());
  }

  public boolean isDrawerOpen(int gravity) {
    return mDrawerLayout.isDrawerOpen(gravity);
  }

  public void closeDrawers() {
    mDrawerLayout.closeDrawers();
  }

  private static class NavigationAdapter extends BaseAdapter
      implements AdapterView.OnItemClickListener {

    private WeakReference<ArtListFragment> mWeakArtListFragment;

    public NavigationAdapter(ArtListFragment fragment) {
      super();
      mWeakArtListFragment = new WeakReference<>(fragment);
    }

    @Override
    public int getCount() {
      return 3;
    }

    @Override
    public Model getItem(int position) {
      Model.Builder builder = new Model.Builder();
      Map<Integer, Action> actions = new HashMap<>();
      switch (position) {
        case 0:
          builder.title("主页");
          builder.cover(EarthUtils.formatString(R.string.img_home));
          actions.put(Const.ACTION_MAIN, new Action.Builder()
              .type(Action.Type.JUMP)
              .clazz(ArtListActivity.class)
              .build());
          break;
        case 1:
          builder.title("收藏");
          builder.cover(EarthUtils.formatString(R.string.img_heart));
          actions.put(Const.ACTION_MAIN, new Action.Builder()
              .type(Action.Type.JUMP)
              .uri(NavigationManager.buildUri(NavigationManager.FAVOURITES))
              .build());
          break;
        case 2:
          builder.title("下载");
          builder.cover(EarthUtils.formatString(R.string.img_down_arrow));
          actions.put(Const.ACTION_MAIN, new Action.Builder()
              .type(Action.Type.JUMP)
              .clazz(DownloadActivity.class)
              .build());
          break;
      }
      builder.actions(actions);
      return builder.build();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      ArtListFragment fragment = mWeakArtListFragment.get();
      if (fragment == null) {
        return;
      }
      Action action = getItem(position).actions.get(Const.ACTION_MAIN);
      fragment.closeDrawers();
      if (action != null) {
        switch (action.type) {
          case JUMP:
            if (null != action.clazz) {
              NavigationManager.navigationTo(view.getContext(), action.clazz, action.bundle);
            } else if (null != action.uri) {
              NavigationManager.navigationTo(view.getContext(), action.uri, action.bundle);
            } else if (null != action.url) {
              NavigationManager.navigationTo(view.getContext(), action.url, action.bundle);
            }
            break;
        }
      }
    }

    private void bindView(View v, int position) {
      TextView title = (TextView) v.findViewById(R.id.title);
      IconFontTextView icon = (IconFontTextView) v.findViewById(R.id.icon);
      Model model = getItem(position);
      title.setText(model.title);
      icon.setText(model.cover);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_navigation, parent, false);
      }
      bindView(convertView, position);
      return convertView;
    }
  }
}
