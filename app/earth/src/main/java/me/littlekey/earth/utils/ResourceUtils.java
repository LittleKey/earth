package me.littlekey.earth.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;

import me.littlekey.earth.EarthApplication;

/**
 * Created by littlekey on 16/6/19.
 */
public class ResourceUtils {

  public static @ColorInt int getColor(@ColorRes int colorRes) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      return EarthApplication.getInstance().getColor(colorRes);
    } else {
      return EarthApplication.getInstance().getResources().getColor(colorRes);
    }
  }

  public static Drawable getDrawable(@DrawableRes int drawRes) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      return EarthApplication.getInstance().getDrawable(drawRes);
    } else {
      return EarthApplication.getInstance().getResources().getDrawable(drawRes);
    }
  }

  public static void setBackground(View view, Drawable drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      view.setBackground(drawable);
    } else {
      view.setBackgroundDrawable(drawable);
    }
  }
}
