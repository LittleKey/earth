package me.littlekey.earth.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.network.ImageManager;
import com.yuanqi.network.NameValuePair;
import com.yuanqi.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
  private Map<String, String> mAdditionHeaders;

  public EarthRequestManager(Context context) {
    super(context);
    mCacheConfig = new CacheConfig(true, DEFAULT_TTL, DEFAULT_SOFT_TTL);
    mAdditionHeaders = new HashMap<>();
    EarthApplication.getInstance().getImageManager().setHeaderCallback(
        new ImageManager.HeaderCallback() {
          @Override
          public List<NameValuePair> getHeaders() {
            List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair(Const.KEY_COOKIE, convertCookies(buildCookie())));
            return headers;
          }
        }
    );
  }

  public void addCookie(String key, String value) {
    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
      mAdditionHeaders.put(key, value);
    }
  }

  public EarthRequest newEarthRequest(
      ApiType apiType, int method,
      Response.Listener<EarthResponse> listener, Response.ErrorListener errorListener) {
    EarthRequest request = new EarthRequest(EarthApplication.getInstance(), method,
        getUrl(apiType), listener, errorListener, mCacheConfig) {

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put(Const.KEY_COOKIE, convertCookies(buildCookie()));
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

  /**
   * Converts List<NameValuePair> to String.
   */
  private String convertCookies(List<NameValuePair> cookies) {
    List<String> result = new ArrayList<>();
    for (NameValuePair cookieField: cookies) {
      CollectionUtils.add(result,
          String.format("%s=%s", cookieField.getName(), cookieField.getValue()));
    }
    return TextUtils.join(";", result);
  }

  private List<NameValuePair> buildCookie() {
    List<NameValuePair> cookies = new ArrayList<>();
    cookies.add(new NameValuePair("ipb_member_id",
        EarthApplication.getInstance().getAccountManager().getUserId()));
    cookies.add(new NameValuePair("ipb_pass_hash",
        EarthApplication.getInstance().getAccountManager().getPassHash()));
    for (Map.Entry<String, String> field: mAdditionHeaders.entrySet()) {
      CollectionUtils.add(cookies, new NameValuePair(field.getKey(), field.getValue()));
    }
    return cookies;
  }
}