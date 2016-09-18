package me.littlekey.earth.model.data;

import android.support.annotation.NonNull;

import com.squareup.wire.Wire;
import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.GetDownloadFileRequest;
import me.littlekey.earth.model.proto.GetDownloadFileResponse;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;
import okio.ByteString;

/**
 * Created by littlekey on 16/7/9.
 */
public class FilesDataGenerator extends EarthDataGenerator<GetDownloadFileResponse> {

  private int mPort;

  public FilesDataGenerator(int port, NameValuePair... pairs) {
    super(ApiType.FILES, pairs);
    mPort = port;
  }

  @Override
  protected ApiRequest<GetDownloadFileResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    GetDownloadFileRequest.Builder builder = new GetDownloadFileRequest.Builder().limit(15);
    long timestamp;
    if ((timestamp = Long.valueOf(Wire.get(pairs.get(Const.KEY_TIME_STAMP), Const.ZERO))) > 0) {
      builder.timestamp(timestamp);
    }
    return EarthApplication.getInstance().getRequestManager()
        .newLocalRequest(EarthUtils.formatString(Const.API_LOCAL_FILES, mPort),
            GetDownloadFileResponse.class,
            ByteString.of(builder.build().encode()),
            mListener, mErrorListener);
  }

  @Override
  public ApiRequest<GetDownloadFileResponse> getNextRequestFromResponse(GetDownloadFileResponse response) {
    Map<String, String> pairs = new HashMap<>();
    pairs.put(Const.KEY_TIME_STAMP, String.valueOf(Wire.get(response.last_timestamp, 0)));
    return onCreateRequest(mApiType, pairs);
  }

  @Override
  public boolean getHasMoreFromResponse(GetDownloadFileResponse response) {
    // NOTE : delegate {@link EarthApiList} determine has more
    return true;
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull GetDownloadFileResponse response) {
    List<Model> models = new ArrayList<>();
    if (!CollectionUtils.isEmpty(response.arts)) {
      for (Art art : response.arts) {
        Model model = ModelFactory.createModelFromArt(art, Model.Template.ITEM_DLC);
        if (model != null) {
          Map<Integer, Action> actions = new HashMap<>(model.actions);
          actions.put(Const.ACTION_DOWNLOAD, new Action.Builder()
              .type(Action.Type.DOWNLOAD)
              .build());
          CollectionUtils.add(models, model.newBuilder()
              .actions(actions)
              .build());
        }
      }
    }
    return models;
  }
}
