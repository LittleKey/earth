package me.littlekey.earth.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.wire.Message;
import com.yuanqi.network.RequestManager;

import java.util.HashMap;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.model.proto.RPCRequest;
import me.littlekey.earth.utils.Const;
import okio.ByteString;

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

  public <T extends Message> VitalityRequest<T> newVitalityProtoRequest(
      ApiType apiType, Map<String, String> params, Class<T> clazz,
      Response.Listener<T> listener, Response.ErrorListener errorListener) {
    return newVitalityProtoRequest(apiType, buildBody(apiType, params), clazz, listener,
        errorListener);
  }

  public <T extends Message> VitalityRequest<T> newVitalityProtoRequest(
      ApiType apiType, ByteString result, Class<T> clazz, Response.Listener<T> listener,
      Response.ErrorListener errorListener) {
    return newVitalityProtoRequest(apiType, buildBody(result), clazz, listener, errorListener);
  }

  private <T extends Message> VitalityRequest<T> newVitalityProtoRequest(
      ApiType apiType, final byte[] body, Class<T> clazz, Response.Listener<T> listener,
      Response.ErrorListener errorListener) {
    VitalityRequest<T> request = new VitalityRequest<T>(EarthApplication.getInstance(),
        Request.Method.POST, getUrl(apiType), clazz, listener, errorListener, mCacheConfig) {

      @Override
      public String getBodyContentType() {
        return "application/protobuf";
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        String sessionId = EarthApplication.getInstance().getAccountManager().getSessionId();
        Map<String, String> headers = new HashMap<>();
        if (sessionId != null) {
          headers.put(Const.NETWORK_HEADER_SSID, sessionId);
        }
        return headers;
      }

      @Override
      public byte[] getBody() {
        return body;
      }
    };
    request.setShouldCache(false);
    return request;
  }

  private String getUrl(ApiType apiType) {
    switch (apiType) {
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
  }

  private byte[] buildBody(ApiType apiType, Map<String, String> params) {
    ByteString content = null;
    switch (apiType) {
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
//    return buildBody(content);
  }

  private byte[] buildBody(ByteString content) {
    RPCRequest.Builder builder =
        new RPCRequest.Builder();
    builder.content(content);
    return RPCRequest.ADAPTER.encode(builder.build());
  }
}