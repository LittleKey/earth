package me.littlekey.earth.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.wire.Message;
import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.network.ImageManager;
import me.littlekey.network.NameValuePair;
import me.littlekey.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.model.proto.RPCRequest;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.PreferenceUtils;
import okio.ByteString;

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
      switch (key) {
        case Const.KEY_S:
        case Const.KEY_LV:
        case Const.KEY_IGNEOUS:
          PreferenceUtils.setString(Const.LAST_COOKIE, key, value);
          break;
      }
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
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put(Const.KEY_COOKIE, convertCookies(buildCookie()));
        headers.put(Const.USER_AGENT, Const.USER_AGENT_VALUE);
        return headers;
      }
    };
    request.setShouldCache(true);
    return request;
  }

  public <T extends Message> LocalRequest<T> newLocalRequest(String url, Class<T> clazz,
      final ByteString body, Response.Listener<T> listener, Response.ErrorListener errorListener) {
    LocalRequest<T> request = new LocalRequest<T>(EarthApplication.getInstance(),
        Request.Method.POST, url,
        clazz, listener, errorListener, mCacheConfig) {

      @Override
      public byte[] getBody() {
        return new RPCRequest.Builder().content(body).build().encode();
      }
    };
    request.setShouldCache(false);
    return request;
  }

  private String getUrl(ApiType apiType) {
    switch (apiType) {
      case TAG_LIST:
      case ART_DETAIL:
      case HOME_LIST:
      case SEARCH_LIST:
      case VIEWER:
        return Const.API_ROOT;
      case SEARCH_FAV_LIST:
        return Const.API_FAV_LIST;
      case LIKED:
        return Const.API_LIKED;
      case LOGIN:
        Map<String, String> loginPairs = new HashMap<>();
        loginPairs.put(Const.KEY_ACT, Const.LOGIN);
        loginPairs.put(Const.KEY_CODE, Const.ZERO_ONE);
        return RequestManager.parseUrl(Const.API_LOGIN, loginPairs);
      case REGISTER:
        Map<String, String> registerPairs = new HashMap<>();
        registerPairs.put(Const.KEY_ACT, Const.REG);
        registerPairs.put(Const.KEY_CODE, Const.ZERO_ZERO);
        return RequestManager.parseUrl(Const.API_REGISTER, registerPairs);
      case CHECK:
        return Const.API_CHECK;
      case FAV_LIST:
        return Const.API_FAV_LIST;
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
  }

  /**
   * Converts Map<String, String> to String.
   */
  public String convertCookies(Map<String, String> cookies) {
    List<String> result = new ArrayList<>();
    for (Map.Entry<String, String> cookieField: cookies.entrySet()) {
      CollectionUtils.add(result,
          String.format("%s=%s", cookieField.getKey(), cookieField.getValue()));
    }
    return TextUtils.join(";", result);
  }

  public Map<String, String> buildCookie() {
    Map<String, String> cookies = new HashMap<>(mAdditionHeaders);
    cookies.put(Const.IPB_MEMBER_ID,
        EarthApplication.getInstance().getAccountManager().getUserId());
    cookies.put(Const.IPB_PASS_HASH,
        EarthApplication.getInstance().getAccountManager().getPassHash());
    cookies.put(Const.UCONFIG, Const.UCONFIG_VALUE);
    return cookies;
  }
}