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
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.event.OnLoadedPageEvent;
import me.littlekey.earth.model.EarthCrawler;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;

/**
 * Created by littlekey on 16/6/16.
 */
public class ArtDataGenerator extends EarthDataGenerator<EarthResponse> {

  private String mBaseUrl;

  public ArtDataGenerator(Bundle bundle, NameValuePair... pairs) {
    super(ApiType.ART_VIEWER, pairs);
    mBaseUrl = bundle.getString(Const.EXTRA_URL);
  }

  @Override
  protected ApiRequest<EarthResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    EarthRequest request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(mBaseUrl, Request.Method.GET, mListener, mErrorListener);
    request.setParams(pairs);
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
    Elements artDetailElements = response.document.select("body > div.gm");
    EventBus.getDefault().post(new OnLoadedPageEvent(mBaseUrl, ModelFactory.createModelFromArt(
        EarthCrawler.createArtDetailFromElements(artDetailElements), Model.Template.DATA)));
    Elements imageElements = response.document.select("#gdt > div.gdtl");
    for (Element imageEle: imageElements) {
      CollectionUtils.add(models, ModelFactory.createModelFromImage(
          EarthCrawler.createImageFromElement(imageEle), Model.Template.Thumbnail));
    }
    return models;
  }
}
