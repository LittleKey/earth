package me.littlekey.earth.presenter;

import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/17.
 */
public class ImagePresenter extends EarthPresenter {

  private ImageRequest mCurrentRequest;
  private boolean mSmoothSwitch;

  public ImagePresenter() {
    this(false);
  }

  public ImagePresenter(boolean smoothSwitch) {
    mSmoothSwitch = smoothSwitch;
  }

  @Override
  public void bind(Model model) {
    Object attrValue = getValueByViewId(id(), model);
    if (attrValue == null) {
      return;
    }
    view().setVisibility(View.VISIBLE);
    if (view() instanceof SimpleDraweeView && attrValue instanceof String) {
      bindImage((SimpleDraweeView) view(), ImageRequest.fromUri((String) attrValue));
    } else if (view() instanceof ImageView && attrValue instanceof Integer) {
      bindImage((ImageView) view(), (Integer) attrValue);
    }
  }

  private Object getValueByViewId(int id, Model model) {
    switch (id) {
      case R.id.cover:
      case R.id.avatar:
      case R.id.icon:
      case R.id.image:
        return model.getCover();
    }
    return null;
  }

  private void bindImage(SimpleDraweeView view, ImageRequest request) {
    PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
        .setOldController(view.getController())
        .setImageRequest(request);
    if (mSmoothSwitch && mCurrentRequest != null) {
      builder.setLowResImageRequest(mCurrentRequest);
    }
    view.setController(builder.build());
    mCurrentRequest = request;
  }

  private void bindImage(ImageView view, int resId) {
    view.setImageResource(resId);
  }
}
