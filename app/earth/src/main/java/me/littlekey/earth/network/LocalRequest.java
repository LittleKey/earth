package me.littlekey.earth.network;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.Wire;
import me.littlekey.network.ApiContext;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.RequestManager;

import java.util.Arrays;

import me.littlekey.earth.model.proto.RPCResponse;
import okio.ByteString;

/**
 * Created by littlekey on 16/7/9.
 */
public class LocalRequest<T extends Message> extends ApiRequest<T> {
  private final Class<T> mClass;
  private final RequestManager.CacheConfig mCacheConfig;

  public LocalRequest(ApiContext apiContext, int method, String url, Class<T> clazz,
                      Response.Listener<T> listener, Response.ErrorListener errorListener,
                      RequestManager.CacheConfig config) {
    super(apiContext, method, url, listener, errorListener);
    this.mClass = clazz;
    this.mCacheConfig = config;
  }

  @Override
  protected T parseResponse(NetworkResponse response) throws Exception {
    RPCResponse pbRPCResponse = RPCResponse.ADAPTER.decode(response.data);
    if (pbRPCResponse.success == null || !pbRPCResponse.success) {
      throw new VolleyError();
    }
    return ProtoAdapter.get(mClass).decode(Wire.get(pbRPCResponse.content, ByteString.EMPTY));
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

  @Override
  public String getBodyContentType() {
    return "application/protobuf";
  }

  @Override
  public byte[] getBody() {
    return new byte[0];
  }

  @Override
  public String getCacheKey() {
    return super.getCacheKey() + Arrays.toString(getBody()).hashCode();
  }
}