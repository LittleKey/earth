package me.littlekey.earth.model.data;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import me.littlekey.base.utils.CollectionUtils;
import me.littlekey.network.ApiRequest;
import me.littlekey.network.NameValuePair;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.event.OnLoadedCommentsEvent;
import me.littlekey.earth.event.OnLoadedPageEvent;
import me.littlekey.earth.network.EarthCrawler;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.Comment;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Image;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;
import timber.log.Timber;

/**
 * Created by littlekey on 16/6/16.
 */
public class ArtDetailDataGenerator extends EarthDataGenerator<EarthResponse> {

  public ArtDetailDataGenerator(List<String> paths, NameValuePair... pairs) {
    super(ApiType.ART_DETAIL, pairs);
    mBasePairs.put(Const.KEY_HC, Const.ONE);
    mPaths = paths;
  }

  @Override
  protected ApiRequest<EarthResponse> onCreateRequest(ApiType apiType, Map<String, String> pairs) {
    EarthRequest request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(ApiType.ART_DETAIL, Request.Method.GET, mListener, mErrorListener);
    for (String path: mPaths) {
      request.appendPath(path);
    }
    request.setQuery(pairs);
    return request;
  }

  @Override
  public ApiRequest<EarthResponse> getNextRequestFromResponse(EarthResponse response) {
    Map<String, String> params = new HashMap<>();
    // page argument was base 0, and website page was base 1. so not need modify page number.
    Count count = null;
    try {
      count = EarthCrawler.createPageCountFromElements(
          response.document.select("table.ptt > tbody > tr > td"));
    } catch (Exception e) {
      Timber.e(EarthUtils.formatString(R.string.parse_error, Const.PAGE_NUMBER));
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
      Timber.e(EarthUtils.formatString(R.string.parse_error, Const.PAGE_NUMBER));
    }
    return count != null && count.number < count.pages;
  }

  @Override
  public List<Model> getItemsFromResponse(@NonNull EarthResponse response) {
    List<Model> models = new ArrayList<>();
    Elements artDetailElements = response.document.select("body > div.gm");
    Art artDetail = null;
    String gallery_id = mPaths.get(1);
    String token = mPaths.get(2);
    try {
      artDetail = EarthCrawler.createArtDetailFromElements(artDetailElements, gallery_id, token);
    } catch (Exception e) {
      Timber.e(EarthUtils.formatString(R.string.parse_error, Const.ART_DETAIL));
    }
    if (artDetail != null) {
    EventBus.getDefault().post(new OnLoadedPageEvent(
        ModelFactory.createModelFromArt(artDetail, Model.Template.DATA)));
    }
    List<Model> comments = new ArrayList<>();
    for (Element commentEle: response.document.select("#cdiv > div.c1")) {
      Comment comment = null;
      try {
        comment = EarthCrawler.createCommentFromElement(commentEle);
      } catch (Exception e) {
        Timber.e(EarthUtils.formatString(R.string.parse_error, Const.COMMENT));
      }
      CollectionUtils.add(comments,
          ModelFactory.createModelFromComment(comment, Model.Template.ITEM_COMMENT));
    }
    EventBus.getDefault().post(new OnLoadedCommentsEvent(gallery_id, comments));
    Elements imageElements = response.document.select("#gdt > div");
    for (Element imageEle: imageElements) {
      Image image = null;
      try {
        image = EarthCrawler.createImageFromElement(imageEle,
            artDetail != null ? artDetail.pages : 0,
            artDetail != null ? artDetail.token : Const.EMPTY_STRING);
      } catch (Exception e) {
        Timber.e(EarthUtils.formatString(R.string.parse_error, Const.IMAGE));
      }
      CollectionUtils.add(models,
          ModelFactory.createModelFromImage(image, Model.Template.PREVIEW_IMAGE));
    }
    return models;
  }
}
