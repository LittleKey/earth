package me.littlekey.earth.model;

import android.support.annotation.NonNull;

import com.yuanqi.mvp.PageList;
import com.yuanqi.network.ApiRequest;
import com.yuanqi.network.NameValuePair;

import me.littlekey.earth.model.data.EarthDataGenerator;
import me.littlekey.earth.network.ApiType;

/**
 * Created by littlekey on 16/6/10.
 */
public class EarthApiList<T> extends PageList<T, Model> {
  private final EarthDataGenerator<T> mDataGenerator;

  public EarthApiList(EarthDataGenerator<T> dataGenerator) {
    super(dataGenerator);
    mDataGenerator = dataGenerator;
  }

  @Override
  protected void onRequestCreated(ApiRequest<T> request, boolean clearData) {
    super.onRequestCreated(request, clearData);
    mDataGenerator.onRequestCreated(request, clearData);
  }

  public void refresh(NameValuePair... pairs) {
    mDataGenerator.resetUrl(pairs);
    refresh();
  }

  public void resetApi(@NonNull ApiType apiType, NameValuePair... pairs) {
    mDataGenerator.resetUrl(apiType, pairs);
    refresh(pairs);
  }
}