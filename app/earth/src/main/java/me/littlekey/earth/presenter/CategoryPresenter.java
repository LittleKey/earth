package me.littlekey.earth.presenter;

import android.support.annotation.ColorRes;
import android.widget.TextView;

import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/16.
 */
public class CategoryPresenter extends EarthPresenter {

  @Override
  public void bind(Model model) {
    Model.Category category = model.getCategory();
    if (category == null) {
      return;
    }
    ((TextView) view()).setText(category.getName());
    view().setBackgroundColor(group().context.getResources().getColor(getColor(category)));
  }

  private @ColorRes int getColor(Model.Category category) {
    switch (category) {
      case DOUJINSHI:
        return R.color.doujinshi;
      case MANGA:
        return R.color.manga;
      case ARTIST_CG:
        return R.color.artist_cg;
      case ASIAN_PORN:
        return R.color.asian_porn;
      case COSPLAY:
        return R.color.cosplay;
      case GAME_CG:
        return R.color.game_cg;
      case IMAGE_SET:
        return R.color.image_set;
      case MISC:
        return R.color.misc;
      case NON__H:
        return R.color.non_h;
      case WESTERN:
        return R.color.western;
    }
    return R.color.transparent;
  }
}
