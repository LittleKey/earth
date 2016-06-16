package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.wire.Wire;
import com.yuanqi.mvp.presenter.ViewGroupPresenter;
import com.yuanqi.mvp.widget.MvpRecyclerView;
import com.yuanqi.network.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.R;
import me.littlekey.earth.adapter.OfflineListAdapter;
import me.littlekey.earth.event.OnClickTagItemEvent;
import me.littlekey.earth.event.OnLoadedPageEvent;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.presenter.EarthPresenterFactory;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.widget.TabLayoutManager;

/**
 * Created by littlekey on 16/6/16.
 */
public class ViewerFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

  private ViewGroupPresenter mPresenter;
  private Model mModel;
  private List<Model> mTags;
  private ViewPager mViewPager;
  private FragmentStatePagerAdapter mPagerAdapter;
  private MvpRecyclerView mRecyclerView;
  private OfflineListAdapter mTabAdapter;
  private TabLayoutManager mTabLayoutManager;
  private int mSelectIndex = -1;

  public static ViewerFragment newInstance(Bundle bundle) {
    ViewerFragment fragment = new ViewerFragment();
    bundle.putInt(Const.KEY_API_TYPE, ApiType.ART_VIEWER.ordinal());
    ArrayList<NameValuePair> pairList = new ArrayList<>();
    pairList.add(new NameValuePair(Const.KEY_P, Const.ZERO));
    bundle.putSerializable(Const.KEY_API_QUERY, pairList);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mPresenter = EarthPresenterFactory.createArtDetailPresenter(container, R.layout.fragment_viewer);
    return mPresenter.view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mModel = getArguments().getParcelable(Const.EXTRA_MODEL);
    if (mModel != null) {
      mPresenter.bind(mModel);
    }
    mTags = new ArrayList<>();
    mRecyclerView = (MvpRecyclerView) view.findViewById(R.id.recycler);
    mTabAdapter = new OfflineListAdapter();
    mTabLayoutManager = new TabLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mTabLayoutManager);
    mRecyclerView.setItemAnimator(null);
    mRecyclerView.setAdapter(mTabAdapter);
    mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
    FragmentManager fm = getChildFragmentManager();
    mPagerAdapter = new FragmentStatePagerAdapter(fm) {
      @Override
      public Fragment getItem(int position) {
        return createFragment(mTags.get(position));
      }

      @Override
      public int getCount() {
        return mTags == null ? 0 :mTags.size();
      }

      @Override
      public CharSequence getPageTitle(int position) {
        return mTags.get(position).getTitle();
      }
    };
    mViewPager.setAdapter(mPagerAdapter);
    EventBus.getDefault().register(this);
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);
    if (fragment == null) {
      fm.beginTransaction()
          .add(R.id.fragment_container, createFragment())
          .commit();
    }
  }

  @Override
  public void onDestroyView() {
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
  }

  @Override
  public void onPageSelected(int position) {
    selectTag(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (state == ViewPager.SCROLL_STATE_IDLE) {
      selectTag(mViewPager.getCurrentItem());
    }
  }

  protected Fragment createFragment(Model tag) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(Const.EXTRA_MODEL, tag);
    return TagFragment.newInstance(bundle);
  }

  protected Fragment createFragment() {
    Bundle bundle = getArguments();
    bundle.putBoolean(Const.KEY_ENABLE_SWIPE_REFRESH, false);
    return ListFragment.newInstance(bundle);
  }

  public void onEventMainThread(OnLoadedPageEvent event) {
    if (mModel != null && TextUtils.equals(event.getBaseUrl(), mModel.getUrl())) {
      mPresenter.bind(event.getModel());
      mTags = event.getModel().getSubModels();
      mTabAdapter.setData(mTags);
      mPagerAdapter.notifyDataSetChanged();
      mViewPager.addOnPageChangeListener(this);
      selectTag(mViewPager.getCurrentItem());
    }
  }

  public void onEventMainThread(OnClickTagItemEvent event) {
    int index;
    Flag flag;
    if ((index = mTags.indexOf(event.getTag())) != -1
        && (flag = mTabAdapter.getItem(index).getFlag()) != null
        && !Wire.get(flag.is_selected, false)) {
      mViewPager.setCurrentItem(index);
    }
  }

  private void selectTag(int index) {
    if (mSelectIndex != index) {
      setItemSelectState(mSelectIndex, false);
      setItemSelectState(index, true);
      if (mTabLayoutManager.findLastCompletelyVisibleItemPosition() != RecyclerView.NO_POSITION
          && (mTabLayoutManager.findFirstCompletelyVisibleItemPosition() < index
            || mTabLayoutManager.findLastCompletelyVisibleItemPosition() > index)) {
        mRecyclerView.smoothScrollToPosition(index);
      }
      mSelectIndex = index;
    }
  }

  private void setItemSelectState(int index, boolean isSelect) {
    if (index < 0 || index >= mTabAdapter.getItemCount()) {
      return;
    }
    Model item = mTabAdapter.getItem(index);
    mTabAdapter.changeData(index, new Model.Builder(item)
        .flag(item.getFlag().newBuilder().is_selected(isSelect).build())
        .build());
  }
}
