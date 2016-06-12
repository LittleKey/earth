package me.littlekey.earth.network;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.yuanqi.network.ApiContext;
import com.yuanqi.network.ApiRequest;
import com.yuanqi.network.RequestManager;

import org.jsoup.Jsoup;

/**
 * Created by littlekey on 16/6/10.
 */
public class EarthRequest extends ApiRequest<EarthResponse> {
  private final RequestManager.CacheConfig mCacheConfig;

  public EarthRequest(ApiContext apiContext, int method, String url,
                      Response.Listener<EarthResponse> listener, Response.ErrorListener errorListener,
                      RequestManager.CacheConfig config) {
    super(apiContext, method, url, listener, errorListener);
    this.mCacheConfig = config;
  }

  @Override
  protected EarthResponse parseResponse(NetworkResponse response) throws Exception {
    return new EarthResponse(
        Jsoup.parse(new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf8"))),
        response.headers);
  }

  @Override
  protected Cache.Entry parseCache(NetworkResponse response) {
    Cache.Entry entry = super.parseCache(response);
    if (mCacheConfig.isPreferLocalConfig()) {
      long now = System.currentTimeMillis();
      entry.ttl = now + mCacheConfig.getTtl();
      entry.softTtl = now + mCacheConfig.getSoftTtl();
    }
    return entry;
  }
}
