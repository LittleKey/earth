package me.littlekey.earth.model.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;

import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.network.EarthCrawler;
import me.littlekey.earth.model.proto.Picture;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;
import me.littlekey.earth.utils.FutureSupplier;
import me.littlekey.earth.utils.PictureTokenFuture;
import me.littlekey.earth.utils.TokenSupplier;

/**
 * Created by littlekey on 16/6/29.
 */
public class PictureLoader extends AsyncTaskLoader<Picture> {

  private int mPosition;
  private String mGid;
  private Picture mResultPicture;
  private WeakReference<TokenSupplier> mWeakTokenSupplier;
  private WeakReference<FutureSupplier<PictureTokenFuture>> mWeakFutureSupplier;

  public PictureLoader(Context context, FutureSupplier<PictureTokenFuture> futureSupplier,
      TokenSupplier tokenSupplier, String gid, int position) {
    super(context);
    mPosition = position;
    mGid = gid;
    mWeakTokenSupplier = new WeakReference<>(tokenSupplier);
    mWeakFutureSupplier = new WeakReference<>(futureSupplier);
  }

  @Override
  protected void onStartLoading() {
    super.onStartLoading();
    if (mResultPicture != null) {
      deliverResult(mResultPicture);
    }
    if (takeContentChanged() || mResultPicture == null) {
      forceLoad();
    }
  }

  @Override
  protected void onStopLoading() {
    super.onStopLoading();
    cancelLoad();
  }

  @Override
  protected void onReset() {
    super.onReset();
    if (mResultPicture != null) {
      mResultPicture = null;
    }
  }

  @Override
  public void onCanceled(Picture data) {
    super.onCanceled(data);
  }

  @Override
  public void deliverResult(Picture data) {
    if (isReset()) {
      return;
    }
    mResultPicture = data;
    if (isStarted()) {
      super.deliverResult(mResultPicture);
    }
  }

  @Override
  public Picture loadInBackground() {
    String token;
    TokenSupplier tokenSupplier = mWeakTokenSupplier.get();
    if (tokenSupplier == null) {
      return null;
    }
    token = tokenSupplier.get(mGid, mPosition);
    if (token == null) {
      FutureSupplier<PictureTokenFuture> futureSupplier = mWeakFutureSupplier.get();
      if (futureSupplier == null) {
        return null;
      }
      PictureTokenFuture pictureTokenFuture = futureSupplier.get(null, mGid, mPosition);
      if (pictureTokenFuture == null) {
        return null;
      }
      try {
        List<String> tokens = pictureTokenFuture.get();
        if (tokens != null) {
          token = tokens.get(mPosition % Const.IMAGE_ITEM_COUNT_PER_PAGE);
        }
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
      if (token == null) {
        return null;
      }
    }
    RequestFuture<EarthResponse> pictureFuture = RequestFuture.newFuture();
    EarthRequest request = EarthApplication.getInstance().getRequestManager().newEarthRequest(
        ApiType.VIEWER, Request.Method.GET, pictureFuture, pictureFuture);
    request.appendPath("s");
    request.appendPath(token);
    request.appendPath(EarthUtils.formatString("%s-%d", mGid, mPosition + 1));
    request.setTag(this);
    request.submit();
    EarthResponse response = null;
    try {
      response = pictureFuture.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    Picture picture = null;
    if (response != null) {
      try {
        Elements pictureElements = response.document.select("#i1");
        // TODO : insert picture token
        picture = EarthCrawler.createPictureFromElements(pictureElements);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return picture;
  }

  @Override
  public void cancelLoadInBackground() {
    super.cancelLoadInBackground();
    EarthApplication.getInstance().getRequestManager().cancel(this);
  }
}
