package me.littlekey.earth.fragment;

import android.database.DataSetObserver;
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
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.mvp.presenter.ViewGroupPresenter;
import com.yuanqi.mvp.widget.MvpRecyclerView;
import com.yuanqi.network.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.R;
import me.littlekey.earth.adapter.OfflineListAdapter;
import me.littlekey.earth.event.OnClickTagItemEvent;
import me.littlekey.earth.event.OnLikedEvent;
import me.littlekey.earth.event.OnLoadedPageEvent;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.presenter.EarthPresenterFactory;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.widget.TagLayoutManager;

/**
 * Created by littlekey on 16/6/16.
 */
public class ArtDetailFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

  private ViewGroupPresenter mPresenter;
  private Model mModel;
  private List<Model> mTags;
  private ViewPager mViewPager;
  private FragmentStatePagerAdapter mPagerAdapter;
  private MvpRecyclerView mTagRecyclerView;
  private OfflineListAdapter mTagAdapter;
  private TagLayoutManager mTagLayoutManager;
  private int mSelectIndex = RecyclerView.NO_POSITION;
  private ViewPager mContentViewPager;
  private FragmentStatePagerAdapter mContentPagerAdapter;
  private List<String> mPaths;

  public static ArtDetailFragment newInstance(Bundle bundle) {
    ArtDetailFragment fragment = new ArtDetailFragment();
    bundle.putInt(Const.KEY_API_TYPE, ApiType.ART_DETAIL.ordinal());
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
    mPresenter = EarthPresenterFactory.createArtDetailPresenter(container, R.layout.fragment_detail);
    return mPresenter.view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mModel = getArguments().getParcelable(Const.EXTRA_MODEL);
    mPaths = getArguments().getStringArrayList(Const.KEY_API_PATH);
    if (mModel != null) {
      mPresenter.bind(mModel);
    }
    mTags = new ArrayList<>();
    mTagRecyclerView = (MvpRecyclerView) view.findViewById(R.id.tag_recycler);
    mTagAdapter = new OfflineListAdapter();
    mTagLayoutManager = new TagLayoutManager(getActivity());
    mTagRecyclerView.setLayoutManager(mTagLayoutManager);
    mTagRecyclerView.setItemAnimator(null);
    mTagRecyclerView.setAdapter(mTagAdapter);

    mViewPager = (ViewPager) view.findViewById(R.id.tag_viewpager);
    FragmentManager fm = getChildFragmentManager();
    mPagerAdapter = new FragmentStatePagerAdapter(fm) {
      @Override
      public Fragment getItem(int position) {
        return createFragment(mTags.get(position));
      }

      @Override
      public int getCount() {
        return mTags == null || !mViewPager.isShown() ? 0 :mTags.size();
      }

      @Override
      public CharSequence getPageTitle(int position) {
        return mTags.get(position).getTitle();
      }
    };
    mViewPager.setAdapter(mPagerAdapter);
    mViewPager.addOnPageChangeListener(this);
    mPagerAdapter.registerDataSetObserver(new DataSetObserver() {
      @Override
      public void onChanged() {
        selectTag(mViewPager.getCurrentItem());
      }

      @Override
      public void onInvalidated() {
        selectTag(mViewPager.getCurrentItem());
      }
    });
    mContentViewPager = (ViewPager) view.findViewById(R.id.content_viewpager);
    mContentPagerAdapter = new FragmentStatePagerAdapter(fm) {
      @Override
      public Fragment getItem(int position) {
        return createFragment(position);
      }

      @Override
      public int getCount() {
        return !CollectionUtils.isEmpty(mPaths) && mPaths.size() >= 2 ? 2 : 1;
      }
    };
    EventBus.getDefault().register(this);
    mContentViewPager.setAdapter(mContentPagerAdapter);
    mContentPagerAdapter.notifyDataSetChanged();
  }

  @Override
  public void onDestroyView() {
    mPresenter.unbind();
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }

  public boolean onBackPressed() {
    // if transition not visible then finish activity without transition animation
    View transitionView = mPresenter.view.findViewById(R.id.cover);
    int[] transitionViewCoordinate = new int[2];
    transitionView.getLocationInWindow(transitionViewCoordinate);
    int[] parentViewCoordinate = new int[2];
    mPresenter.view.getLocationInWindow(parentViewCoordinate);
    if (transitionViewCoordinate[1] - parentViewCoordinate[1] + transitionView.getHeight() < 0) {
      getActivity().finish();
      return true;
    }
    return false;
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

  protected Fragment createFragment(int position) {
    Bundle bundle;
    switch (position) {
      case 0:
        bundle = getArguments();
        bundle.putBoolean(Const.KEY_ENABLE_SWIPE_REFRESH, false);
        return ListFragment.newInstance(bundle);
      case 1:
        bundle = new Bundle();
        bundle.putString(Const.EXTRA_IDENTITY, mPaths.get(1));
        return CommentFragment.newInstance(bundle);
    }
    return null;
  }

  @SuppressWarnings("unused")
  public void onEventMainThread(OnLoadedPageEvent event) {
    if (!CollectionUtils.isEmpty(mPaths) && mPaths.size() >= 2
        && TextUtils.equals(event.getModel().getIdentity(), mPaths.get(1))) {
      Map<Integer, Action> actions = new HashMap<>();
      if (mModel != null) {
        actions.putAll(mModel.getActions());
      }
      actions.putAll(event.getModel().getActions());
      actions.put(Const.ACTION_SHOW_HIDE, new Action.Builder()
          .type(Action.Type.RUNNABLE)
          .runnable(new Runnable() {
            @Override
            public void run() {
              showViewPager(!mViewPager.isShown());
            }
          }).build());
      mModel = new Model.Builder(event.getModel())
          .flag(event.getModel().getFlag().newBuilder().is_selected(false).build())
          .actions(actions)
          .build();
      mPresenter.bind(mModel);
      mTags = mModel.getSubModels();
      mTagAdapter.setData(mTags);
      mSelectIndex = RecyclerView.NO_POSITION;
    }
  }

  @SuppressWarnings("unused")
  public void onEventMainThread(OnClickTagItemEvent event) {
    int index;
    Flag flag;
    if ((index = mTags.indexOf(event.getTag())) != -1
        && (flag = mTagAdapter.getItem(index).getFlag()) != null
        && !Wire.get(flag.is_selected, false)) {
      showViewPager(true);
      mViewPager.setCurrentItem(index);
    }
  }

  @SuppressWarnings("unused")
  public void onEventMainThread(OnLikedEvent event) {
    if (mModel != null && TextUtils.equals(mModel.getIdentity(), event.getIdentity())) {
      mModel = new Model.Builder(mModel)
          .flag(mModel.getFlag().newBuilder().is_liked(event.isLiked()).build())
          .art(mModel.getArt().newBuilder().liked(event.isLiked()).build())
          .build();
      mPresenter.bind(mModel);
    }
  }

  private void showViewPager(boolean show) {
    mModel = new Model.Builder(mModel)
        .flag(mModel.getFlag().newBuilder().is_selected(show).build())
        .build();
    mPresenter.bind(mModel);
    mViewPager.setVisibility(show ? View.VISIBLE : View.GONE);
    mPagerAdapter.notifyDataSetChanged();
  }

  private void selectTag(int index) {
    if (mSelectIndex != index) {
      setItemSelectState(mSelectIndex, false);
      setItemSelectState(index, true);
      if (mTagLayoutManager.findLastCompletelyVisibleItemPosition() != RecyclerView.NO_POSITION
          && (mTagLayoutManager.findFirstCompletelyVisibleItemPosition() < index
            || mTagLayoutManager.findLastCompletelyVisibleItemPosition() > index)) {
        mTagRecyclerView.smoothScrollToPosition(index);
      }
      mSelectIndex = index;
    }
  }

  private void setItemSelectState(int index, boolean isSelect) {
    if (index < 0 || index >= mTagAdapter.getItemCount()) {
      return;
    }
    Model item = mTagAdapter.getItem(index);
    mTagAdapter.changeData(index, new Model.Builder(item)
        .flag(item.getFlag().newBuilder().is_selected(isSelect).build())
        .build());
  }
}
