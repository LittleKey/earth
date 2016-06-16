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
        .add(R.id.cover, new ImagePresenter())
        .add(R.id.title, new BasePresenter())
        .add(R.id.subtitle, new BasePresenter())
        .add(R.id.rating, new BasePresenter())
        .add(R.id.category, new CategoryPresenter())
        .add(R.id.date, new BasePresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createArtDetailPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.cover, new ImagePresenter(true))
        .add(R.id.title, new BasePresenter())
        .add(R.id.subtitle, new BasePresenter())
        .add(R.id.category, new CategoryPresenter(true))
        .add(R.id.language, new BasePresenter())
        .add(R.id.page_number, new BasePresenter())
        .add(R.id.size, new BasePresenter())
        .add(R.id.likes, new BasePresenter())
        .add(R.id.date, new BasePresenter());
  }

  public static ViewGroupPresenter createChildTagPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.title, new TagPresenter(R.drawable.blue_circle_background))
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createParentTagPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.title, new TagPresenter(R.drawable.red_circle_background))
        .add(R.id.title, new FlagPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createThumbnailPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.image, new ImagePresenter())
        .add(R.id.number, new BasePresenter());
  }
}
