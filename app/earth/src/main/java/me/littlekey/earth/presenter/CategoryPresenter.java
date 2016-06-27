package me.littlekey.earth.presenter;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.widget.TextView;

import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.utils.ResourceUtils;

/**
 * Created by littlekey on 16/6/16.
 */
public class CategoryPresenter extends EarthPresenter {

  private boolean mReverse = false;
  private @ColorInt int mTextColor = 0;

  public CategoryPresenter() {
    this(false);
  }

  public CategoryPresenter(boolean reverse) {
    mReverse = reverse;
  }

  @Override
  public void bind(Model model) {
    Model.Category category = model.category;
    if (category == null) {
      return;
    }
    if (mTextColor == 0) {
      mTextColor = ((TextView) view()).getCurrentTextColor();
    }
    ((TextView) view()).setText(category.getName());
    @ColorInt int backgroundColor = ResourceUtils.getColor(getColor(category));
    ((TextView) view()).setTextColor(mReverse ? backgroundColor : mTextColor);
    view().setBackgroundColor(mReverse ? mTextColor : backgroundColor);
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
