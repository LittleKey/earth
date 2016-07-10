package me.littlekey.earth.model.data;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.squareup.wire.Wire;
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
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;
import timber.log.Timber;

/**
 * Created by littlekey on 16/6/12.
 */
public class ArtListsDataGenerator extends EarthDataGenerator<EarthResponse> {

  public ArtListsDataGenerator(ApiType apiType, List<String> paths, NameValuePair... pairs) {
    super(apiType, pairs);
    mPaths = paths;
  }

  @Override
  protected ApiRequest<EarthResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    EarthRequest request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(apiType, Request.Method.GET, mListener, mErrorListener);
    switch (apiType) {
      case HOME_LIST:
      case SEARCH_LIST:
      case FAV_LIST:
        request.setQuery(pairs);
        break;
      case TAG_LIST:
        assert mPaths != null;
        for (String path: mPaths) {
          request.appendPath(path);
        }
        request.appendPath(Wire.get(pairs.get(Const.KEY_PAGE), Const.EMPTY_STRING));
        break;
    }
    return request;
  }

  @Override
  public ApiRequest<EarthResponse> getNextRequestFromResponse(EarthResponse response) {
    Map<String, String> pairs = new HashMap<>(mBasePairs);
    // page argument was base 0, and website page was base 1. so not need modify page number.
    Count count = null;
    try {
      count = EarthCrawler.createPageCountFromElements(response.document.select("table.ptt > tbody > tr > td"));
    } catch (Exception e) {
      Timber.e(EarthUtils.formatString(R.string.parse_error, Const.PAGE_NUMBER));
    }
    if (count != null) {
      pairs.put(Const.KEY_PAGE, String.valueOf(count.number));
      return onCreateRequest(mApiType, pairs);
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
      Timber.e(EarthUtils.formatString(R.string.parse_error, Const.PAGE_NUMBER));
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
        } catch (Exception e) {
          Timber.e(EarthUtils.formatString(R.string.parse_error, Const.ART_ITEM));
        }
        CollectionUtils.add(models, ModelFactory.createModelFromArt(art, Model.Template.ITEM_ART));
      }
    }
    return models;
  }
}
