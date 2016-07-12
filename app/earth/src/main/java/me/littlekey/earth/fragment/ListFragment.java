package me.littlekey.earth.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.base.utils.FormatUtils;
import com.yuanqi.mvp.adapter.HeaderFooterAdapter;
import com.yuanqi.mvp.widget.MvpRecyclerView;
import com.yuanqi.network.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import me.littlekey.earth.R;
import me.littlekey.earth.adapter.ListAdapter;
import me.littlekey.earth.model.DataGeneratorFactory;
import me.littlekey.earth.model.EarthApiList;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.widget.EarthSwipeRefreshLayout;

/**
 * Created by littlekey on 16/6/10.
 */
public class ListFragment extends BaseFragment {

  private final static int ART_DETAIL_GRID_SPAN = 2;

  private EarthSwipeRefreshLayout mSwipeRefreshLayout;
  private MvpRecyclerView mRecyclerView;
  private EarthApiList<?> mList;

  public static ListFragment newInstance(Bundle bundle) {
    ListFragment fragment = new ListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(getLayout(), container, false);
    mSwipeRefreshLayout = (EarthSwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
    mRecyclerView = (MvpRecyclerView) rootView.findViewById(R.id.recycler);
    return rootView;
  }

  @Override
  public void onViewCreated(final View view, final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ApiType apiType;
    int apiTypeNum = getArguments().getInt(Const.KEY_API_TYPE, -1);
    if (apiTypeNum >= 0 && apiTypeNum < ApiType.values().length) {
      apiType = ApiType.values()[apiTypeNum];
    } else {
      throw new IllegalStateException("Api type can not be null.");
    }
    @SuppressWarnings("unchecked")
    ArrayList<NameValuePair> pairList =
        (ArrayList<NameValuePair>) getArguments().getSerializable(Const.KEY_API_QUERY);
    NameValuePair[] pairs;
    if (pairList == null) {
      pairs = new NameValuePair[0];
    } else {
      pairs = pairList.toArray(new NameValuePair[pairList.size()]);
    }
    resetApi(apiType, getArguments().getStringArrayList(Const.KEY_API_PATH), pairs);
    mSwipeRefreshLayout.setEnabled(getArguments().getBoolean(Const.KEY_ENABLE_SWIPE_REFRESH, true));
  }

  @Override
  public void onDestroyView() {
    mList.unregisterDataLoadObservers();
    super.onDestroyView();
  }

  public void insertItemAndScroll(int position, Model model) {
    if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
      ((ListAdapter) mRecyclerView.getAdapter()).insertData(position, model);
      mRecyclerView.scrollToPosition(position);
    }
  }

  public void deleteItem(Model model) {
    if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
      ((ListAdapter) mRecyclerView.getAdapter()).removeData(model);
    }
  }

  public void deleteRangeItem(Model model, int count) {
    if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
      ((ListAdapter) mRecyclerView.getAdapter()).removeRangeData(model, count);
    }
  }

  public void changeItem(Model model, int position) {
    if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
      ((ListAdapter) mRecyclerView.getAdapter()).changeData(position, model);
    }
  }

  protected List<Model> getDataSet() {
    return ((ListAdapter) mRecyclerView.getAdapter()).getData();
  }

  protected int getLayout() {
    return R.layout.fragment_list;
  }

  private void setEmptyViewCheckListener(final ListAdapter adapter) {
    adapter.setOnCheckEmptyListener(new HeaderFooterAdapter.CheckEmptyListener() {
      @Override
      public boolean onCheckEmpty(HeaderFooterAdapter.ViewData emptyView, int headerCount,
                                  int dataCount, int footerCount) {
        boolean isEmpty = super.onCheckEmpty(emptyView, headerCount, dataCount, footerCount);

        if (isEmpty) {
          for (Model model : mList.getItems()) {
            adapter.removeData(model);
          }
        }
        return isEmpty;
      }
    });
  }

  public void smoothScrollToPosition(int position) {
    if (mRecyclerView != null) {
      mRecyclerView.smoothScrollToPosition(position);
    }
  }

  public void refresh(NameValuePair... pairs) {
    mList.refresh(pairs);
  }

  public @Nullable String getUrl() {
    return mList == null ? null : mList.getCurrentUrl();
  }

  public void resetApi(@NonNull ApiType apiType, List<String> paths, NameValuePair... pairs) {
    mList = new EarthApiList<>(DataGeneratorFactory.createDataGenerator(apiType, paths, pairs));
    final ListAdapter adapter = new ListAdapter(getArguments());
    mList.registerDataLoadObserver(adapter);
    mList.registerDataLoadObserver(mSwipeRefreshLayout);
    mSwipeRefreshLayout.setAdapter(adapter);
    adapter.setList(mList);
    mList.refresh();
    final RecyclerView.LayoutManager layoutManager;
    switch (apiType) {
      case ART_DETAIL:
        layoutManager = new GridLayoutManager(getActivity(), ART_DETAIL_GRID_SPAN) {
          @Override
          protected int getExtraLayoutSpace(RecyclerView.State state) {
            // for preload more image
            return Math.max(super.getExtraLayoutSpace(state), 3 * getHeight());
          }
        };
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(
            new GridLayoutManager.SpanSizeLookup() {
              @Override
              public int getSpanSize(int position) {
                if (position >= adapter.size()) {
                  // NOTE : footer or header
                  return ART_DETAIL_GRID_SPAN;
                }
                int view_type = adapter.getDataItemViewType(position);
                Model.Template template = Model.Template.values()[view_type];
                switch (template) {
                  case PREVIEW_IMAGE:
                    return ART_DETAIL_GRID_SPAN / 2;
                  default:
                    return ART_DETAIL_GRID_SPAN / 2;
                }
              }
            }
        );
        // NOTE : add top margin on first item
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
          @Override
          public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) < 2) {
              outRect.top = FormatUtils.dipsToPix(10);
            }
          }
        });
        break;
      case LIKED:
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
          @Override
          public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
              outRect.top = FormatUtils.dipsToPix(10);
            } else {
              outRect.top = FormatUtils.dipsToPix(5);
            }
            if (position == layoutManager.getItemCount() - 1) {
              outRect.bottom = FormatUtils.dipsToPix(10);
            } else {
              outRect.bottom = FormatUtils.dipsToPix(5);
            }
          }
        });
        break;
      case HOME_LIST:
      case TAG_LIST:
      case SEARCH_LIST:
      case SEARCH_FAV_LIST:
      case FAV_LIST:
        /** TODO : code refactoring
         *  {@link android.support.design.widget.SearchAppBarBehavior}
         *  {@link android.support.design.widget.SearchScrollBehavior}
         */
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(),
            Const.ART_LIST_TOP_PADDING,
            mRecyclerView.getPaddingRight(),
            mRecyclerView.getPaddingBottom());
        mSwipeRefreshLayout.setProgressViewOffset(true,
            Const.ART_LIST_TOP_PADDING - EarthSwipeRefreshLayout.DEFAULT_CIRCLE_TARGET,
            Const.ART_LIST_TOP_PADDING + EarthSwipeRefreshLayout.DEFAULT_CIRCLE_TARGET);
        // NOTE : don't break, let it go to default
      default:
        layoutManager = new LinearLayoutManager(getActivity());
    }
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setItemAnimator(null);
  }
}
