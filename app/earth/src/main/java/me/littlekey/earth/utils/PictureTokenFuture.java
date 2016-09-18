package me.littlekey.earth.utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import me.littlekey.base.utils.CollectionUtils;

import org.jsoup.nodes.Element;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;

/**
 * Created by littlekey on 16/6/30.
 */
public class PictureTokenFuture implements Future<List<String>> {

  private RequestFuture<EarthResponse> mDelegate;
  private WeakReference<TokenSupplier> mWeakTokenSupplier;
  private String gToken;
  private String gid;
  private int page;
  private List<String> mResultTokens;

  public PictureTokenFuture(TokenSupplier tokenSupplier, String gToken, String gid, int page) {
    mDelegate = RequestFuture.newFuture();
    mWeakTokenSupplier = new WeakReference<>(tokenSupplier);
    mResultTokens = null;
    EarthRequest request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(ApiType.ART_DETAIL, Request.Method.GET, mDelegate, mDelegate);
    request.appendPath("g");
    request.appendPath(this.gid = gid);
    request.appendPath(this.gToken = gToken);
    Map<String, String> pairs = new HashMap<>();
    pairs.put(Const.KEY_P, String.valueOf(this.page = page));
    request.setQuery(pairs);
    request.submit();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof PictureTokenFuture) {
      PictureTokenFuture pictureTokenFuture = (PictureTokenFuture) o;
      return gToken != null && gid != null
          && TextUtils.equals(gToken, pictureTokenFuture.gToken)
          && TextUtils.equals(gid, pictureTokenFuture.gid)
          && page == pictureTokenFuture.page;
    }
    return false;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return mDelegate.cancel(mayInterruptIfRunning);
  }

  @Override
  public boolean isCancelled() {
    return mDelegate.isCancelled();
  }

  @Override
  public boolean isDone() {
    return mDelegate.isDone();
  }

  @Override
  public List<String> get() throws InterruptedException, ExecutionException {
    try {
      return doGet(null);
    } catch (TimeoutException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public List<String> get(long timeout, @NonNull TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return doGet(TimeUnit.MILLISECONDS.convert(timeout, unit));
  }

  private synchronized List<String> doGet(Long timeoutMs)
      throws InterruptedException, ExecutionException, TimeoutException {
    if (mResultTokens != null) {
      return mResultTokens;
    }
    EarthResponse response;
    if (timeoutMs != null) {
      response = mDelegate.get(timeoutMs, TimeUnit.MILLISECONDS);
    } else {
      response = mDelegate.get();
    }
    List<String> tokens = new ArrayList<>();
    for (Element element: response.document.select("#gdt > div > a")) {
      List<String> paths = Uri.parse(element.attr("href")).getPathSegments();
      if (!CollectionUtils.isEmpty(paths)) {
        tokens.add(paths.get(1));
      }
    }
    TokenSupplier tokenSupplier = mWeakTokenSupplier.get();
    if (tokenSupplier != null) {
      tokenSupplier.insertPage(gid, page, tokens);
    }
    return Collections.unmodifiableList(mResultTokens = tokens);
  }
}