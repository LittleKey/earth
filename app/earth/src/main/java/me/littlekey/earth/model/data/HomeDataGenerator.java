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
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;

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
    return request;
  }

  @Override
  public ApiRequest<EarthResponse> getNextRequestFromResponse(EarthResponse response) {
    Map<String, String> params = new HashMap<>();
    // page argument was base 0, and website page was base 1. so not need modify page number.
    params.put(Const.KEY_PAGE,
        response.document.select("table.ptt > tbody > tr > td.ptds > a").text());
    return onCreateRequest(mApiType, params);
  }

  @Override
  public boolean getHasMoreFromResponse(EarthResponse response) {
    Elements pageElements = response.document.select("table.ptt > tbody > tr > td");
    int currentPage = Integer.valueOf(pageElements.select("td.ptds > a").text());
    int totalPage = Integer.valueOf(pageElements.get(pageElements.size() - 2).select("a").text());

    return currentPage < totalPage;
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull EarthResponse response) {
    List<Model> models = new ArrayList<>();
    Elements elements = response.document.select("table.itg tr");
    // NOTE : remove first unused element
    elements.remove(0);
    for (Element element: elements) {
      CollectionUtils.add(models, ModelFactory.createModelFromArt(
          EarthCrawler.createArtItemFromElement(element), Model.Template.ITEM_ART));
    }
    return models;
  }
}
