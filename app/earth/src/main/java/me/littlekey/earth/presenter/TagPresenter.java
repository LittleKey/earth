package me.littlekey.earth.presenter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.TextView;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/18.
 */
public class TagPresenter extends EarthPresenter {

  private Drawable mDrawable;

  public TagPresenter(@DrawableRes int drawableId) {
    mDrawable = EarthApplication.getInstance().getResources().getDrawable(drawableId);
  }

  @Override
  public void bind(Model model) {
    String tagName = model.getTitle();
    if (tagName == null) {
      return;
    }
    view().setVisibility(View.VISIBLE);
    if (view() instanceof TextView) {
      bindText((TextView) view(), tagName);
    }
  }

  private void bindText(TextView view, CharSequence tagName) {
    view.setText(tagName);
    if (Build.VERSION_CODES.JELLY_BEAN <= Build.VERSION.SDK_INT) {
      view.setBackground(mDrawable);
    } else {
      view.setBackgroundDrawable(mDrawable);
    }
  }
}
