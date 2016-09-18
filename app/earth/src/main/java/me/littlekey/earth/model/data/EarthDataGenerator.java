package me.littlekey.earth.model.data;

import android.support.annotation.NonNull;

import com.android.volley.Response;
import me.littlekey.mvp.DataGenerator;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.model.Model;
import me.littlekey.earth.network.ApiType;

/**
 * Created by littlekey on 16/6/10.
 */
public abstract class EarthDataGenerator<R> implements DataGenerator<R, Model> {
  private boolean mEnableCache = false;
  protected ApiType mApiType;
  protected Map<String, String> mBasePairs;
  protected List<String> mPaths;
  protected ApiRequest<R> mInitRequest;
  protected Response.Listener<R> mListener;
  protected Response.ErrorListener mErrorListener;

  public EarthDataGenerator(@NonNull ApiType apiType, NameValuePair... pairs) {
    init(apiType, pairs);
  }

  private void init(@NonNull ApiType apiType, NameValuePair... pairs) {
    this.mApiType = apiType;
    this.mBasePairs = new HashMap<>();
    if (pairs != null) {
      for (NameValuePair pair : pairs) {
        mBasePairs.put(pair.getName(), pair.getValue());
      }
    }
  }

  @Override
  public ApiRequest<R> onCreateRequest() {
    if (mInitRequest == null) {
      mInitRequest = onCreateRequest(mApiType, mBasePairs);
    }
    return mInitRequest;
  }

  protected abstract ApiRequest<R> onCreateRequest(ApiType apiType, Map<String, String> pairs);

  public void resetPairs(NameValuePair... pairs) {
    init(mApiType, pairs);
  }

  public void setEnableCache(boolean enableCache) {
    this.mEnableCache = enableCache;
  }

  public void onRequestCreated(ApiRequest<R> request, boolean clearData) {
    if (request == null) {
      return;
    }
    if ((request.getUrl() == null || request.equals(mInitRequest)) && mEnableCache) {
      if (clearData) {
        request.getCache().invalidate(request.getCacheKey(), false);
      }
      request.setShouldCache(true);
      request.setEnableMultiResponse(true);
    } else {
      request.setShouldCache(false);
    }
  }

  @Override
  public final void setListener(Response.Listener<R> listener) {
    mListener = listener;
  }

  @Override
  public final void setErrorListener(Response.ErrorListener errorListener) {
    mErrorListener = errorListener;
  }
}