package me.littlekey.earth.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    resetApi(apiType, getArguments().getBundle(Const.KEY_BUNDLE), pairs);
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

  public void refresh(NameValuePair... pairs) {
    mList.refresh(pairs);
  }

  public void resetApi(@NonNull ApiType apiType, NameValuePair... pairs) {
    resetApi(apiType, null, pairs);
  }

  public void resetApi(@NonNull ApiType apiType, Bundle bundle, NameValuePair... pairs) {
    mList = new EarthApiList<>(DataGeneratorFactory.createDataGenerator(apiType, bundle, pairs));
    final ListAdapter adapter = new ListAdapter(getArguments());
    mList.registerDataLoadObserver(adapter);
    mList.registerDataLoadObserver(mSwipeRefreshLayout);
    mSwipeRefreshLayout.setAdapter(adapter);
    adapter.setList(mList);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mRecyclerView.setItemAnimator(null);
  }
}
