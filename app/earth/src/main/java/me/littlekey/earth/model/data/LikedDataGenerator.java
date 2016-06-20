package me.littlekey.earth.model.data;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.network.ApiRequest;
import com.yuanqi.network.NameValuePair;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.model.EarthCrawler;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Fav;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;
import timber.log.Timber;

/**
 * Created by littlekey on 16/6/21.
 */
public class LikedDataGenerator extends EarthDataGenerator<EarthResponse> {

  public LikedDataGenerator(NameValuePair... pairs) {
    super(ApiType.LIKED, pairs);
  }

  @Override
  protected ApiRequest<EarthResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    EarthRequest request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(ApiType.LIKED, Request.Method.POST, mListener, mErrorListener);
    request.setQuery(pairs);
    return request;
  }

  @Override
  public ApiRequest<EarthResponse> getNextRequestFromResponse(EarthResponse response) {
    return null;
  }

  @Override
  public boolean getHasMoreFromResponse(EarthResponse response) {
    return false;
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull EarthResponse response) {
    List<Model> models = new ArrayList<>();
    Elements elements = response.document.select("#galpop > div > div.nosel > div");
    String apply = response.document.select("[name=apply]").attr("value");
    String gid = mBasePairs.get(Const.KEY_GID);
    String token = mBasePairs.get(Const.KEY_T);
    for (Element element: elements) {
      Fav fav = null;
      try {
        fav = EarthCrawler.createFavFromElement(element, apply);
      } catch (Exception e) {
        Timber.e(EarthUtils.formatString(R.string.parse_error, Const.FAV));
      }
      Map<Integer, Action> actions = new HashMap<>();
      Bundle bundle = new Bundle();
      bundle.putString(Const.KEY_GID, gid);
      bundle.putString(Const.KEY_TOKEN, token);
      actions.put(Const.ACTION_MAIN, new Action.Builder()
          .type(Action.Type.LIKED)
          .bundle(bundle)
          .build());
      CollectionUtils.add(models,
          new Model.Builder(ModelFactory.createModelFromFav(fav, Model.Template.SELECT_FAV))
              .actions(actions)
              .build());
    }
    return models;
  }
}
