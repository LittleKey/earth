package me.littlekey.earth.utils;

import android.net.Uri;

import com.squareup.wire.Wire;
import com.yuanqi.base.utils.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.GetDownloadFileRequest;
import me.littlekey.earth.model.proto.GetDownloadFileResponse;
import me.littlekey.earth.model.proto.RPCRequest;
import me.littlekey.earth.model.proto.RPCResponse;
import okio.ByteString;

/**
 * Created by littlekey on 16/7/8.
 */
public class EarthServer extends NanoHTTPD {

  public EarthServer() throws IOException {
    super(0);
    start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
  }

  @Override
  public Response serve(IHTTPSession session) {
    RPCResponse.Builder responseBuilder = new RPCResponse.Builder().success(false);
    Map<String, String> headers = new HashMap<>();
    if (session.getMethod() == Method.POST) {
      List<String> paths = Uri.parse(session.getUri()).getPathSegments();
      if (!CollectionUtils.isEmpty(paths)) {
        try {
          Map<String, String> files = new HashMap<>();
          session.parseBody(files);
          ByteString postBody = ByteString.decodeBase64(files.get("postBody"));
          switch (paths.get(0)) {
            case "arts":
              RPCRequest rpcRequest = RPCRequest.ADAPTER.decode(postBody);
              GetDownloadFileRequest getDownloadFileRequest =
                  GetDownloadFileRequest.ADAPTER.decode(rpcRequest.content);
              long timestamp = Wire.get(getDownloadFileRequest.timestamp, 0L);
              int limit = Wire.get(getDownloadFileRequest.limit, 15);
              long[] last_timestamp = new long[] {timestamp};
              List<Art> arts = EarthApplication.getInstance().getFileManager()
                  .artList(timestamp, limit, last_timestamp);
              GetDownloadFileResponse response = new GetDownloadFileResponse.Builder()
                  .last_timestamp(last_timestamp[0]).arts(arts)
                  .build();
              responseBuilder.success(true)
                  .content(ByteString.of(response.encode()));
              break;
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    Response response = new Response(Response.Status.OK, "application/protocol",
        new ByteArrayInputStream(responseBuilder.build().encode()), -1);
    for (Map.Entry<String, String> header: headers.entrySet()) {
      response.addHeader(header.getKey(), header.getValue());
    }
    return response;
  }
}
