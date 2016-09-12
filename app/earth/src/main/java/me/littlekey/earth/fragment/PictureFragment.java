package me.littlekey.earth.fragment;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.loader.PictureLoader;
import me.littlekey.earth.model.proto.Picture;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;
import timber.log.Timber;

/**
 * Created by littlekey on 16/6/29.
 */
public class PictureFragment extends BaseFragment implements LoaderManager.LoaderCallbacks {

  private SimpleDraweeView mPictureView;
  private String mPictureSrc;
  private boolean mVisible;

  public static PictureFragment newInstance(String gid, String gToken, int position) {
    PictureFragment fragment = new PictureFragment();
    Bundle bundle = new Bundle();
    bundle.putString(Const.KEY_GID, gid);
    bundle.putString(Const.KEY_TOKEN, gToken);
    bundle.putInt(Const.KEY_POSITION, position);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_picture, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    mPictureView = (SimpleDraweeView) view.findViewById(R.id.picture);
    mPictureView.setHierarchy(GenericDraweeHierarchyBuilder.newInstance(getResources())
        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
        .setProgressBarImage(new ProgressBarDrawable()).build());
    if (savedInstanceState != null) {
      mPictureSrc = savedInstanceState.getString(Const.EXTRA_URL);
    } else {
      File galleryDir = new File(EarthApplication.getInstance().getFileManager().getDownloadDir(),
          EarthUtils.formatString(R.string.art_file_name,
              getArguments().getString(Const.KEY_GID),
              getArguments().getString(Const.KEY_TOKEN)));
      File picFile = new File(galleryDir, EarthUtils.formatString("%d.jpg", getArguments().getInt(Const.KEY_POSITION)));
      if (picFile.exists() && picFile.isFile()) {
        mPictureSrc = Uri.fromFile(picFile).toString();
      }
    }
    if (mPictureSrc != null) {
        setPictureSrc(mPictureSrc);
    } else {
      view.post(new Runnable() {
        @Override
        public void run() {
          getLoaderManager().initLoader(0, getArguments(), PictureFragment.this);
        }
      });
    }
  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);
    mVisible = menuVisible;
    if (mPictureView == null) {
      return;
    }
    DraweeController controller = mPictureView.getController();
    if (controller != null) {
      Animatable animatable = controller.getAnimatable();
      if (animatable != null) {
        if (menuVisible) {
          animatable.start();
        } else {
          animatable.stop();
        }
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mPictureSrc != null) {
      outState.putString(Const.EXTRA_URL, mPictureSrc);
    }
  }

  private void setPictureSrc(String src) {
    ControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {

      @Override
      public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
        if (animatable != null && mVisible) {
          animatable.start();
        }
      }
    };
    PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder()
        .setUri(src)
        .setTapToRetryEnabled(true)
        .setOldController(mPictureView.getController())
        .setControllerListener(controllerListener);
    mPictureView.setController(controllerBuilder.build());
    mPictureSrc = src;
    Timber.d(EarthUtils.formatString("Position: %d, url: %s", getArguments().getInt(Const.KEY_POSITION), src));
  }

  @Override
  public void onLoaderReset(Loader loader) {
    Timber.d("onLoaderReset");
  }

  @Override
  public void onLoadFinished(Loader loader, Object data) {
    if (data instanceof Picture) {
      Model model = ModelFactory.createModelFromPicture((Picture) data, Model.Template.PAGE_PICTURE);
      if (model != null) {
        setPictureSrc(model.cover);
      }
    }
  }

  @Override
  public Loader onCreateLoader(int id, Bundle args) {
    return new PictureLoader(getActivity(),
        (ViewerFragment) getParentFragment(),
        (ViewerFragment) getParentFragment(),
        args.getString(Const.KEY_GID),
        args.getInt(Const.KEY_POSITION));
  }
}
