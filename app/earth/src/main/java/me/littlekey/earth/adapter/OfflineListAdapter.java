package me.littlekey.earth.adapter;

import android.view.ViewGroup;

import com.squareup.wire.Wire;
import com.yuanqi.mvp.presenter.ViewGroupPresenter;
import com.yuanqi.mvp.widget.MvpRecyclerView;

import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.presenter.EarthPresenterFactory;

/**
 * Created by littlekey on 16/6/18.
 */
public class OfflineListAdapter extends MvpRecyclerView.Adapter<Model> {

  public OfflineListAdapter() {
    super();
  }

  @Override
  protected ViewGroupPresenter onCreateViewPresenter(ViewGroup parent, int viewType) {
    Model.Template template = Wire.get(Model.Template.fromValue(viewType), Model.Template.UNSUPPORTED);
    switch (template) {
      case PARENT_TAG:
        return EarthPresenterFactory.createParentTagPresenter(parent, R.layout.item_parent_tag);
      case CHILD_TAG:
        return EarthPresenterFactory.createChildTagPresenter(parent, R.layout.item_child_tag);
      case CATEGORY:
        return EarthPresenterFactory.createCategoryItemPresenter(parent, R.layout.item_category, this);
      case TITLE:
        return EarthPresenterFactory.createTitlePresenter(parent, R.layout.item_title);
      case ITEM_COMMENT:
        return EarthPresenterFactory.createCommentItemPresenter(parent, R.layout.item_comment);
      case ADVANCED_SEARCH:
        return EarthPresenterFactory.createAdvSearchItemPresenter(parent, R.layout.item_adv_search, this);
      case RATING_SELECT:
        return EarthPresenterFactory.createPopupRatingPresenter(parent, R.layout.popup_rating, this);
      case QUICK_SEARCH:
        return EarthPresenterFactory.createQuickSearchItemPresenter(parent, R.layout.item_quick_search);
      default:
        throw new IllegalStateException("Nonsupport template : " + template.name());
    }
  }

  @Override
  public int getItemViewType(int position) {
    return getItem(position).template.getValue();
  }
}