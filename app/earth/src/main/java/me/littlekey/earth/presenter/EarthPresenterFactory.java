package me.littlekey.earth.presenter;

import android.view.ViewGroup;

import com.yuanqi.mvp.presenter.ViewGroupPresenter;

import me.littlekey.earth.R;

/**
 * Use this factory to build Presenter, so that you can make all change in one class.<br/>
 * Created by nengxiangzhou on 15/5/8.
 */
public class EarthPresenterFactory {
  private EarthPresenterFactory() {}

  public static ViewGroupPresenter createEmptyPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createArtItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.cover, new BasePresenter())
        .add(R.id.title, new BasePresenter())
        .add(R.id.subtitle, new BasePresenter())
        .add(R.id.rating, new BasePresenter())
        .add(R.id.category, new CategoryPresenter())
        .add(R.id.date, new BasePresenter());
  }
}
