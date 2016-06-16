package me.littlekey.earth.adapter;

import android.view.ViewGroup;

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
    Model.Template template = Model.Template.values()[viewType];
    switch (template) {
      case PARENT_TAG:
        return EarthPresenterFactory.createParentTagPresenter(parent, R.layout.item_parent_tag);
      case CHILD_TAG:
        return EarthPresenterFactory.createChildTagPresenter(parent, R.layout.item_child_tag);
      default:
        throw new IllegalStateException("Nonsupport template : " + template.name());
    }
  }

  @Override
  public int getItemViewType(int position) {
    return getItem(position).getTemplate().ordinal();
  }
}