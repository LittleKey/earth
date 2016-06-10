package me.littlekey.earth.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import me.littlekey.earth.EarthApplication;

/**
 * Created by littlekey on 16/6/10.
 */
public class ToastUtils {

  private static Toast mInstance;

  private ToastUtils() {}

  public static void toast(@StringRes int resId) {
    toast(EarthApplication.getInstance().getResources().getText(resId));
  }

  public static void toast(CharSequence content) {

    if (mInstance == null) {
      synchronized (ToastUtils.class) {
        mInstance = Toast.makeText(EarthApplication.getInstance(), content, Toast.LENGTH_SHORT);
      }
    }
    mInstance.setText(content);
    mInstance.show();
  }
}