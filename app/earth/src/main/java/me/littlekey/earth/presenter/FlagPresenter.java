package me.littlekey.earth.presenter;


import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.wire.Wire;
import me.littlekey.base.utils.CollectionUtils;

import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;
import me.littlekey.earth.utils.ResourceUtils;

/**
 * Created by nengxiangzhou on 15/5/8.
 */
public class FlagPresenter extends EarthPresenter {
  @Override
  public void bind(Model model) {
    switch (id()) {
    /** Common **/
      case R.id.mask:
        judgeMask(model);
        break;
      case R.id.show_hide_fab:
        judgeShowHide(model);
        break;
      case R.id.likes:
        judgeLiked(model);
        break;
      case R.id.black_background:
        judgeBlackBackgroundSelected(model);
        break;
      case R.id.checkbox:
        judgeCheckBox(model);
        break;
      default:
        judgeSelected(model);
        break;
    }
  }

  private void judgeCheckBox(Model model) {
    if (view() instanceof CheckBox) {
      ((CheckBox) view()).setChecked(Wire.get(model.flag.is_selected, false));
    }
  }

  private void judgeLiked(Model model) {
    if (view() instanceof TextView) {
      Drawable drawable = EarthUtils.setDrawableBounds(ResourceUtils.getDrawable(
          Wire.get(model.flag.is_liked, false) ? R.drawable.liked : R.drawable.unlike));
      ((TextView) view()).setCompoundDrawables(drawable, null, null, null);
    }
  }

  private void judgeShowHide(Model model) {
    if (view() instanceof FloatingActionButton) {
      ((FloatingActionButton) view()).setImageDrawable(ResourceUtils.getDrawable(
          Wire.get(model.flag.is_selected, false) ?
              R.drawable.arrow_up_gray : R.drawable.arrow_down_gray));
    }
  }

  private void judgeMask(Model model) {
    if (view() instanceof ImageView) {
      Action action = model.actions.get(Const.ACTION_MAIN);
      boolean isSelected = model.flag != null && Wire.get(model.flag.is_selected, false);
      if (action != null) {
        switch (action.type) {
          case SELECT_REGION:
            ((ImageView) view()).setImageResource(isSelected ? R.drawable.green_mask : R.drawable.unselect);
            return;
          case NEXT_REGION:
            ((ImageView) view()).setImageResource(isSelected ? R.drawable.arrow_down_gray : R.drawable.arrow_up_gray);
            return;
          default:
            break;
        }
      }
      ((ImageView) view()).setImageResource(isSelected ? R.drawable.green_mask : 0);
    }
  }

  private void judgeSelected(Model model) {
    view().setVisibility(model.flag.is_selected ? View.VISIBLE : View.GONE);
  }

  private void judgeBlackBackgroundSelected(Model model) {
    view().setVisibility(Wire.get(model.flag.is_selected, false) ? View.GONE : View.VISIBLE);
  }

  @SuppressWarnings("unused")
  private void judgeHasMore(Model model) {
    if (view() instanceof TextView) {
      if (CollectionUtils.isEmpty(model.subModels)) {
        ((TextView) view()).setCompoundDrawables(null, null, null, null);
      } else {
        Drawable drawable = ResourceUtils.getDrawable(R.drawable.arrow_right);
        if (drawable != null) {
          ((TextView) view()).setCompoundDrawables(null, null,
              EarthUtils.setDrawableBounds(drawable), null);
        }
      }
    }
  }
}
