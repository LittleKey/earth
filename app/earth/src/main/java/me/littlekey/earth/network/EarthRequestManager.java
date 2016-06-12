package me.littlekey.earth.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.yuanqi.network.RequestManager;

import java.util.HashMap;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.utils.Const;

/**
 * Created by littlekey on 16/6/10.
 */
public class EarthRequestManager extends RequestManager {
  private static final long DEFAULT_TTL = 7 * 24 * 60 * 60 * 1000; // 7 days
  private static final long DEFAULT_SOFT_TTL = 5 * 60 * 1000; // 5 minutes
  private final CacheConfig mCacheConfig;

  public EarthRequestManager(Context context) {
    super(context);
    mCacheConfig = new CacheConfig(true, DEFAULT_TTL, DEFAULT_SOFT_TTL);
  }

  public EarthRequest newEarthRequest(
      ApiType apiType, int method,
      Response.Listener<EarthResponse> listener, Response.ErrorListener errorListener) {
    EarthRequest request = new EarthRequest(EarthApplication.getInstance(), method,
        getUrl(apiType), listener, errorListener, mCacheConfig) {

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        String cookie = String.format("ipb_member_id=%s;ipb_pass_hash=%s",
            EarthApplication.getInstance().getAccountManager().getUserId(),
            EarthApplication.getInstance().getAccountManager().getPassHash());
        headers.put(Const.KEY_COOKIE, cookie);
        return headers;
      }
    };
    request.setShouldCache(false);
    return request;
  }

  private String getUrl(ApiType apiType) {
    switch (apiType) {
      case HOME_LIST:
        return Const.API_HOME_LIST;
      case LOGIN:
        return Const.API_LOGIN;
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
  }
}