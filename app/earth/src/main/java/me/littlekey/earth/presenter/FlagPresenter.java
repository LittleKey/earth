package me.littlekey.earth.presenter;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.wire.Wire;
import com.yuanqi.base.utils.CollectionUtils;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;

/**
 * Created by nengxiangzhou on 15/5/8.
 */
public class FlagPresenter extends EarthPresenter {
  @Override
  public void bind(Model model) {
    switch (id()) {
    /** Common **/
      case R.id.title:
        judgeSelected(model);
        break;
      case R.id.mask:
        judgeMask(model);
        break;
    }
  }

  private void judgeMask(Model model) {
    if (view() instanceof ImageView) {
      Action action = model.getActions().get(Const.ACTION_MAIN);
      boolean isSelected = model.getFlag() != null && Wire.get(model.getFlag().is_selected, false);
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
    if (view() instanceof TextView) {
      ((TextView) view()).setTextColor(model.getFlag().is_selected
          ? EarthApplication.getInstance().getResources().getColor(R.color.red)
          : EarthApplication.getInstance().getResources().getColor(R.color.black));
      return;
    }
    view().setVisibility(model.getFlag().is_selected ? View.VISIBLE : View.GONE);
  }

  private void judgeHasMore(Model model) {
    if (view() instanceof TextView) {
      if (CollectionUtils.isEmpty(model.getSubModels())) {
        ((TextView) view()).setCompoundDrawables(null, null, null, null);
      } else {
        Drawable drawable = view().getResources().getDrawable(R.drawable.arrow_right);
        if (drawable != null) {
          ((TextView) view()).setCompoundDrawables(null, null,
              EarthUtils.setDrawableBounds(drawable), null);
        }
      }
    }
  }
}
