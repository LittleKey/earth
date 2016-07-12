package me.littlekey.earth.model;

import android.support.annotation.Nullable;

import com.yuanqi.mvp.PageList;
import com.yuanqi.network.ApiRequest;
import com.yuanqi.network.NameValuePair;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.model.data.EarthDataGenerator;
import me.littlekey.earth.model.data.FilesDataGenerator;

/**
 * Created by littlekey on 16/6/10.
 */
public class EarthApiList<T> extends PageList<T, Model> {
  private final EarthDataGenerator<T> mDataGenerator;
  private String mCurrentUrl;

  public EarthApiList(EarthDataGenerator<T> dataGenerator) {
    super(dataGenerator);
    mDataGenerator = dataGenerator;
  }

  @Override
  protected void onRequestCreated(ApiRequest<T> request, boolean clearData) {
    super.onRequestCreated(request, clearData);
    mDataGenerator.onRequestCreated(request, clearData);
    mCurrentUrl = request.getUrl();
  }

  public void refresh(NameValuePair... pairs) {
    mDataGenerator.resetPairs(pairs);
  }

  @Override
  public boolean hasMore() {
    if (mDataGenerator instanceof FilesDataGenerator) {
      return  getItems().size() < EarthApplication.getInstance().getFileManager().size();
    }
    return super.hasMore();
  }

  public @Nullable String getCurrentUrl() {
    return mCurrentUrl;
  }
}