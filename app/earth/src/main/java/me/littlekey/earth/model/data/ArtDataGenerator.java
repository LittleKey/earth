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

import de.greenrobot.event.EventBus;
import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.event.OnLoadedPageEvent;
import me.littlekey.earth.model.EarthCrawler;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Image;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import timber.log.Timber;

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
    Map<String, String> params = new HashMap<>();
    // page argument was base 0, and website page was base 1. so not need modify page number.
    Count count = null;
    try {
      count = EarthCrawler.createPageCountFromElements(response.document.select("table.ptt > tbody > tr > td"));
    } catch (Exception e) {
      Timber.e("parse page number error");
    }
    if (count != null) {
      params.put(Const.KEY_P, String.valueOf(count.number));
      return onCreateRequest(mApiType, params);
    }
    return null;
  }

  @Override
  public boolean getHasMoreFromResponse(EarthResponse response) {
    Elements pageElements = response.document.select("table.ptt > tbody > tr > td");
    Count count = null;
    try {
      count = EarthCrawler.createPageCountFromElements(pageElements);
    } catch (Exception e) {
      Timber.e(e, "parse page number error");
    }
    return count != null && count.number < count.pages;
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull EarthResponse response) {
    List<Model> models = new ArrayList<>();
    Elements artDetailElements = response.document.select("body > div.gm");
    Art artDetail = null;
    try {
      artDetail =EarthCrawler.createArtDetailFromElements(artDetailElements);
    } catch (Exception e) {
      Timber.e(e, "parse art detail error");
    }
    if (artDetail != null) {
    EventBus.getDefault().post(new OnLoadedPageEvent(mBaseUrl,
        ModelFactory.createModelFromArt(artDetail, Model.Template.DATA)));
    }
    Elements imageElements = response.document.select("#gdt > div");
    for (Element imageEle: imageElements) {
      Image image = null;
      try {
            image = EarthCrawler.createImageFromElement(imageEle);
      } catch (Exception e) {
        Timber.e(e, "parse image error");
      }
      CollectionUtils.add(models, ModelFactory.createModelFromImage(image, Model.Template.PREVIEW_IMAGE));
    }
    return models;
  }
}
