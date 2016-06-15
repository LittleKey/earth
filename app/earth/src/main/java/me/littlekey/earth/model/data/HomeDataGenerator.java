package me.littlekey.earth.model.data;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.network.ApiRequest;
import com.yuanqi.network.NameValuePair;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthResponse;

/**
 * Created by littlekey on 16/6/12.
 */
public class HomeDataGenerator extends EarthDataGenerator<EarthResponse> {

  public HomeDataGenerator(NameValuePair... pairs) {
    super(ApiType.HOME_LIST, pairs);
  }

  @Override
  protected ApiRequest<EarthResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    return EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(apiType, Request.Method.GET, mListener, mErrorListener);
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
    Elements elements = response.document.select("table.itg tr");
    // NOTE : remove first unused element
    elements.remove(0);
    for (Element element: elements) {
      CollectionUtils.add(models, ModelFactory.createModelFromArtElement(element, Model.Template.ITEM_ART));
    }
    return models;
  }
}
