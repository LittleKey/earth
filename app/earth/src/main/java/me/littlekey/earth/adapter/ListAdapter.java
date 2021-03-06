package me.littlekey.earth.adapter;

import android.os.Bundle;
import android.view.ViewGroup;

import com.squareup.wire.Wire;
import me.littlekey.mvp.adapter.MvpAdapter;
import me.littlekey.mvp.presenter.ViewGroupPresenter;

import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.presenter.EarthPresenterFactory;

/**
 * Created by littlekey on 16/6/10.
 */
public class ListAdapter extends MvpAdapter<Model> {

  private Bundle mBundle;

  public ListAdapter() {
    super();
  }

  public ListAdapter(Bundle bundle) {
    this();
    this.mBundle = bundle;
  }

  @Override
  protected ViewGroupPresenter onCreateDataViewPresenter(ViewGroup parent, int viewType) {
    Model.Template template = Wire.get(Model.Template.fromValue(viewType), Model.Template.UNSUPPORTED);
    switch (template) {
      case ITEM_ART:
        return EarthPresenterFactory.createArtItemPresenter(parent, R.layout.item_art);
      case PREVIEW_IMAGE:
        return EarthPresenterFactory.createThumbnailPresenter(parent, R.layout.item_preview_image, this);
      case SELECT_FAV:
        return EarthPresenterFactory.createFavSelectPresenter(parent, R.layout.item_select_fav);
      case ITEM_DLC:
        return EarthPresenterFactory.createDLCItemPresenter(parent, R.layout.item_dlc, this);
      case ITEM_DLC_DOWNLOADING:
        return EarthPresenterFactory.createDownloadingDLCItemPresenter(parent, R.layout.item_downloading_dlc, this);
      default:
        throw new IllegalStateException("Nonsupport template : " + template.name());
    }
  }

  @Override
  public int getDataItemViewType(int position) {
    return getItem(position).template.getValue();
  }

}
