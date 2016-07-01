package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

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

  public static PictureFragment newInstance(String gid, int position) {
    PictureFragment fragment = new PictureFragment();
    Bundle bundle = new Bundle();
    bundle.putString(Const.KEY_GID, gid);
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
        .setProgressBarImage(new ProgressBarDrawable()).build());
    // TODO : determine picture whether exist in download location
    if (savedInstanceState != null
        && (mPictureSrc = savedInstanceState.getString(Const.EXTRA_URL)) != null) {
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
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mPictureSrc != null) {
      outState.putString(Const.EXTRA_URL, mPictureSrc);
    }
  }

  private void setPictureSrc(String src) {
    PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder()
        .setUri(src)
        .setTapToRetryEnabled(true)
        .setOldController(mPictureView.getController());
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
