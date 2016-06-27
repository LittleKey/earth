package me.littlekey.earth.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.squareup.wire.Wire;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/17.
 */
public class ImagePresenter extends EarthPresenter {

  private ImageRequest mLowResRequest;
  private boolean mSmoothSwitch;

  public ImagePresenter() {
    this(false);
  }

  public ImagePresenter(boolean smoothSwitch) {
    mSmoothSwitch = smoothSwitch;
  }

  @Override
  public void bind(Model model) {
    view().setVisibility(View.VISIBLE);
    if (view() instanceof SimpleDraweeView) {
      bindImage((SimpleDraweeView) view(), model);
    }
  }

  private void bindImage(SimpleDraweeView view, Model model) {
    ImageRequestBuilder requestBuilder = ImageRequestBuilder
        .newBuilderWithSource(Uri.parse(model.cover));
    if (Wire.get(model.flag.is_thumbnail, false)) {
      requestBuilder.setPostprocessor(new ThumbnailAdjustProcessor(model));
      requestBuilder.setResizeOptions(
          new ResizeOptions(model.count.width, model.count.height));
    }
    PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder()
        .setOldController(view.getController());
    if (mSmoothSwitch && mLowResRequest != null) {
      controllerBuilder.setLowResImageRequest(mLowResRequest);
    }
    controllerBuilder.setImageRequest(requestBuilder.build());
    if (mSmoothSwitch && mLowResRequest == null) {
      mLowResRequest = controllerBuilder.getImageRequest();
    }
    view.setController(controllerBuilder.build());
  }

  private static class ThumbnailAdjustProcessor extends BasePostprocessor {

    private Model mModel;

    public ThumbnailAdjustProcessor(Model model) {
      mModel = model;
    }

    @Override
    public CacheKey getPostprocessorCacheKey() {
      return new SimpleCacheKey(mModel.url + mModel.count.x_offset);
    }

    @Override
    public String getName() {
      return "thumbnail_processor";
    }

    @Override
    public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
      int x_offset = mModel.count.x_offset;
      int width = mModel.count.width;
      int height = mModel.count.height;
      CloseableReference<Bitmap> bitmapRef = bitmapFactory.createBitmap(width, height);
      try {
        Bitmap destBitmap = bitmapRef.get();
        int dest_x = 0;
        for (int x = 0; x < sourceBitmap.getWidth(); x++) {
          if (x < -x_offset) continue;
          if (x >= width - x_offset) break;
          for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            destBitmap.setPixel(dest_x, y, sourceBitmap.getPixel(x, y));
          }
          dest_x++;
        }
        return CloseableReference.cloneOrNull(bitmapRef);
      } finally {
        CloseableReference.closeSafely(bitmapRef);
      }
    }
  }
}
