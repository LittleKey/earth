package me.littlekey.earth.model.data;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

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
import me.littlekey.earth.model.EarthCrawler;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import timber.log.Timber;

/**
 * Created by littlekey on 16/6/12.
 */
public class HomeDataGenerator extends EarthDataGenerator<EarthResponse> {

  private String mBaseUrl;

  public HomeDataGenerator(Bundle bundle, NameValuePair... pairs) {
    super(ApiType.HOME_LIST, pairs);
    if (bundle != null) {
      mBaseUrl = bundle.getString(Const.EXTRA_URL);
    }
  }

  @Override
  protected ApiRequest<EarthResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    ApiRequest<EarthResponse> request;
    if (TextUtils.isEmpty(mBaseUrl)) {
      request = EarthApplication.getInstance().getRequestManager()
          .newEarthRequest(apiType, Request.Method.GET, mListener, mErrorListener);
    } else {
      request = EarthApplication.getInstance().getRequestManager()
          .newEarthRequest(mBaseUrl, Request.Method.GET, mListener, mErrorListener);
    }
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
      params.put(Const.KEY_PAGE, String.valueOf(count.number));
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
    Elements elements = response.document.select("table.itg tr");
    if (elements.size() > 0) {
      // NOTE : remove first unused element
      elements.remove(0);
      for (Element element : elements) {
        Art art = null;
        try {
          art = EarthCrawler.createArtItemFromElement(element);
          ;
        } catch (Exception e) {
          Timber.e(e, "parse art item error");
        }
        CollectionUtils.add(models, ModelFactory.createModelFromArt(art, Model.Template.ITEM_ART));
      }
    }
    return models;
  }
}
