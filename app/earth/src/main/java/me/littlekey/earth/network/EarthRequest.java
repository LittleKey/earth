package me.littlekey.earth.network;

import android.net.Uri;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.yuanqi.network.ApiContext;
import com.yuanqi.network.ApiRequest;
import com.yuanqi.network.RequestManager;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.utils.EarthUtils;

/**
 * Created by littlekey on 16/6/10.
 */
public class EarthRequest extends ApiRequest<EarthResponse> {
  private final RequestManager.CacheConfig mCacheConfig;
  private Map<String, String> mPairs;
  private final List<String> mPaths;

  public EarthRequest(ApiContext apiContext, int method, String url,
                      Response.Listener<EarthResponse> listener, Response.ErrorListener errorListener,
                      RequestManager.CacheConfig config) {
    super(apiContext, method, url, listener, errorListener);
    this.mCacheConfig = config;
    mPaths = new ArrayList<>();
  }

  public void setQuery(Map<String, String> pairs) {
    mPairs = pairs;
  }

  public void appendPath(String path) {
    mPaths.add(path);
  }

  public Map<String, String> getQuery() {
    return mPairs;
  }

  @Override
  public String getUrl() {
    Uri.Builder builder = Uri.parse(super.getUrl()).buildUpon();
    for (String path: mPaths) {
      builder.appendPath(path);
    }
    if (mPairs != null) {
      for (Map.Entry<String, String> entry : mPairs.entrySet()) {
        builder.appendQueryParameter(entry.getKey(), entry.getValue());
      }
    }
    return builder.build().toString();
  }

  @Override
  protected EarthResponse parseResponse(NetworkResponse response) throws Exception {
    for (String field: HttpHeaderParser.parseSetCookie(response.headers)) {
      Map<String, String> fieldMap = parseSetCookieField(field);
      Set<String> fieldKeys = fieldMap.keySet();
      if (fieldKeys.contains("expires")) {
        fieldKeys.remove("path");
        fieldKeys.remove("domain");
        fieldKeys.remove("max-age");
        fieldKeys.remove("expires");
        for (String key: fieldKeys) {
          EarthApplication.getInstance().getRequestManager().addCookie(key, fieldMap.get(key));
        }
      }
    }
    return new EarthResponse(
        Jsoup.parse(new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf8"))),
        response.headers);
  }

  @Override
  protected Cache.Entry parseCache(NetworkResponse response) {
    Cache.Entry entry = super.parseCache(response);
    if (entry != null && mCacheConfig.isPreferLocalConfig()) {
      long now = System.currentTimeMillis();
      entry.ttl = now + mCacheConfig.getTtl();
      entry.softTtl = now + mCacheConfig.getSoftTtl();
    }
    return entry;
  }

  private Map<String, String> parseSetCookieField(String field) {
    Map<String, String> result = new HashMap<>();
    for (String pairString: field.split(";")) {
      String[] pair = pairString.split("=");
      result.put(EarthUtils.strip(pair[0]).toLowerCase(), EarthUtils.strip(pair[1]).toLowerCase());
    }
    return result;
  }
}
