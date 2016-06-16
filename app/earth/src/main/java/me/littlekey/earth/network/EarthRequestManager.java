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

  public EarthRequest newEarthRequest(ApiType apiType, int method,
      Response.Listener<EarthResponse> listener, Response.ErrorListener errorListener) {
    return newEarthRequest(getUrl(apiType), method, listener, errorListener);
  }

  public EarthRequest newEarthRequest(String url, int method,
      Response.Listener<EarthResponse> listener, Response.ErrorListener errorListener) {
    EarthRequest request = new EarthRequest(EarthApplication.getInstance(), method,
        url, listener, errorListener, mCacheConfig) {

      @Override
      public String getUrl() {
        try {
          if (getMethod() == Method.POST || getMethod() == Method.PUT) {
            return super.getUrl();
          }
          return RequestManager.parseUrl(super.getUrl(), getParams());
        } catch (AuthFailureError ignore) {
          ignore.printStackTrace();
        }
        return null;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put(Const.KEY_COOKIE, convertCookies(buildCookie()));
        return headers;
      }
    };
    request.setShouldCache(true);
    return request;
  }

  private String getUrl(ApiType apiType) {
    switch (apiType) {
      case HOME_LIST:
        return Const.API_HOME_LIST;
      case LOGIN:
        Map<String, String> loginPairs = new HashMap<>();
        loginPairs.put(Const.KEY_ACT, Const.LOGIN);
        loginPairs.put(Const.KEY_CODE, Const.ZERO_ONE);
        return RequestManager.parseUrl(Const.API_LOGIN, loginPairs);
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
  }

  /**
   * Converts Map<String, String> to String.
   */
  private String convertCookies(Map<String, String> cookies) {
    List<String> result = new ArrayList<>();
    for (Map.Entry<String, String> cookieField: cookies.entrySet()) {
      CollectionUtils.add(result,
          String.format("%s=%s", cookieField.getKey(), cookieField.getValue()));
    }
    return TextUtils.join(";", result);
  }

  private Map<String, String> buildCookie() {
    Map<String, String> cookies = new HashMap<>(mAdditionHeaders);
    cookies.put("ipb_member_id",
        EarthApplication.getInstance().getAccountManager().getUserId());
    cookies.put("ipb_pass_hash",
        EarthApplication.getInstance().getAccountManager().getPassHash());
    cookies.put("uconfig", "uh_y-xr_a-rx_0-ry_0-tl_r-ar_0-dm_l-prn_y-cats_6-fs_f-xns_0-xl_null-rc_0-lt_m-ts_l-tr_2-cs_a-sc_0-to_a-pn_1-hp_-hk_-tf_n-oi_n-qb_n-ms_n-mt_n");
    return cookies;
  }
}