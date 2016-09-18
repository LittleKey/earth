package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.view.View;

import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.mvp.DataLoadObserver;
import me.littlekey.network.NameValuePair;

import java.util.ArrayList;

import me.littlekey.earth.model.DataGeneratorFactory;
import me.littlekey.earth.model.EarthApiList;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.utils.Const;

/**
 * Created by littlekey on 16/6/10.
 */
public abstract class AsyncLoadFragment extends BaseFragment implements DataLoadObserver<Model> {

  private EarthApiList<?> mList;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ApiType apiType;
    int apiTypeNum = getArguments().getInt(Const.KEY_API_TYPE, -1);
    if (apiTypeNum >= 0 && apiTypeNum < ApiType.values().length) {
      apiType = ApiType.values()[apiTypeNum];
    } else {
      throw new IllegalStateException("Api type can not be null.");
    }
    @SuppressWarnings("unchecked")
    ArrayList<NameValuePair> pairs =
        (ArrayList<NameValuePair>) getArguments().getSerializable(Const.KEY_API_QUERY);
    if (pairs == null) {
      pairs = new ArrayList<>();
    }
    mList = new EarthApiList<>(DataGeneratorFactory.createDataGenerator(apiType,
        getArguments().getStringArrayList(Const.KEY_API_PATH),
        pairs.toArray(new NameValuePair[pairs.size()])));
    mList.registerDataLoadObserver(this);
    mList.refresh();
  }

  @Override
  public void onDestroyView() {
    if (mList != null) {
      mList.unregisterDataLoadObservers();
    }
    super.onDestroyView();
  }

  public void refresh() {
    if (mList != null) {
      mList.refresh();
    }
  }

  @Override
  public final void onLoadSuccess(Op op, OpData<Model> opData) {
    if (CollectionUtils.isEmpty(opData.newData)) {
      onLoadError(op, new Exception("No model get from response."));
      return;
    }
    onLoadSuccess(op, opData.newData.get(0));
  }

  abstract protected void onLoadSuccess(Op op, Model model);
}