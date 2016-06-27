package me.littlekey.earth.presenter;

import android.widget.TextView;

import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.utils.ResourceUtils;

/**
 * Created by littlekey on 16/6/18.
 */
public class TagPresenter extends EarthPresenter {

  private boolean mIsSelectable;

  public TagPresenter selectable(boolean selectable) {
    mIsSelectable = selectable;
    return this;
  }

  @Override
  public void bind(Model model) {
    if (view() instanceof TextView) {
      bindText((TextView) view(), model);
    }
  }

  private void bindText(TextView view, Model model) {
    view.setText(model.title);
    if (mIsSelectable) {
      view.setTextColor(ResourceUtils.getColor(model.flag.is_selected ?
          R.color.white : R.color.half_transparent_white));
    }
    switch (model.template) {
      case CHILD_TAG:
        view.setBackgroundResource(R.drawable.blue_circle_background);
        break;
      case PARENT_TAG:
        view.setBackgroundResource(R.drawable.red_circle_background);
        break;
    }
  }
}
