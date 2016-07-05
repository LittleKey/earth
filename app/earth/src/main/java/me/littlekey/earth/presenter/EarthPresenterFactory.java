package me.littlekey.earth.presenter;

import android.view.ViewGroup;

import com.yuanqi.mvp.presenter.ViewGroupPresenter;
import com.yuanqi.mvp.widget.MvpRecyclerView;

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
        .add(R.id.title, new TextPresenter())
        .add(R.id.subtitle, new TextPresenter())
        .add(R.id.rating, new BasePresenter())
        .add(R.id.category, new CategoryPresenter())
        .add(R.id.date, new TextPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createArtDetailPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.cover, new ImagePresenter(true))
        .add(R.id.cover, new ActionPresenter())
        .add(R.id.title, new TextPresenter())
        .add(R.id.subtitle, new TextPresenter())
        .add(R.id.category, new CategoryPresenter(true))
        .add(R.id.language, new TextPresenter())
        .add(R.id.page_number, new TextPresenter())
        .add(R.id.size, new TextPresenter())
        .add(R.id.likes, new TextPresenter())
        .add(R.id.likes, new FlagPresenter())
        .add(R.id.likes, new ActionPresenter())
        .add(R.id.date, new TextPresenter())
        .add(R.id.fab, new ActionPresenter())
        .add(R.id.fab, new FlagPresenter())
        .add(R.id.rating, new BasePresenter())
        .add(R.id.rating_count, new TextPresenter());
  }

  public static ViewGroupPresenter createChildTagPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.title, new TagPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createParentTagPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.title, new TagPresenter().selectable(true))
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createThumbnailPresenter(ViewGroup parent, int layout,
      MvpRecyclerView.Adapter adapter) {
    return new ViewGroupPresenter(parent, layout, adapter)
        .add(R.id.image, new ImagePresenter())
        .add(R.id.number, new TextPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createFavSelectPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.title, new TextPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createCategoryItemPresenter(ViewGroup parent, int layout,
      MvpRecyclerView.Adapter adapter) {
    return new ViewGroupPresenter(parent, layout, adapter)
        .add(R.id.title, new CategoryPresenter())
        .add(R.id.black_background, new FlagPresenter())
        .add(new ActionPresenter());
  }

  public static ViewGroupPresenter createTitlePresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.title, new TextPresenter());
  }

  public static ViewGroupPresenter createCommentItemPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(R.id.title, new TextPresenter())
        .add(R.id.content, new TextPresenter())
        .add(R.id.date, new TextPresenter());
  }
}
