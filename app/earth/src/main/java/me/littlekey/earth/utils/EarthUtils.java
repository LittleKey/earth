package me.littlekey.earth.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import me.littlekey.earth.EarthApplication;

/**
 * Created by littlekey on 16/6/10.
 */
public class EarthUtils {
  public static final int SEXAGESIMAL = 60;
  public static final int DATE_TODAY = 0;
  public static final int DATE_YESTERDAY = 1;
  public static final int DATE_WITHIN_A_WEEK = 2;
  public static final int DATE_BEYONG_A_WEEK = 3;
  public static final int DATE_SPECIFIC_DATE = 4;

  private EarthUtils() {
  }

  public static String formatString(@StringRes int format, Object... args) {
    return formatString(EarthApplication.getInstance().getString(format), args);
  }

  public static String formatString(String format, Object... args) {
    return String.format(Locale.US, format, args);
  }

  /**
   * Get screen orientation
   *
   * @param activity Activity, for get WindowManager.
   * @return return if unknown will return portrait or landscape without reverse
   * {@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}
   * {@link ActivityInfo#SCREEN_ORIENTATION_REVERSE_LANDSCAPE}
   * {@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}
   * {@link ActivityInfo#SCREEN_ORIENTATION_REVERSE_PORTRAIT}
   */
  public static int getScreenOrientation(Activity activity) {
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    DisplayMetrics dm = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    int height = dm.heightPixels;
    int orientation;
    if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && height > width ||
        (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && width > height) {
      switch (rotation) {
        case Surface.ROTATION_0:
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          break;
        case Surface.ROTATION_90:
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          break;
        case Surface.ROTATION_180:
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
          break;
        case Surface.ROTATION_270:
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
          break;
        default:
          Log.e(activity.getClass().getName(),
              "Unknown screen orientation. Defaulting to portrait.");
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          break;
      }
    } else {
      switch (rotation) {
        case Surface.ROTATION_0:
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          break;
        case Surface.ROTATION_90:
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          break;
        case Surface.ROTATION_180:
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
          break;
        case Surface.ROTATION_270:
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
          break;
        default:
          Log.e(activity.getClass().getName(),
              "Unknown screen orientation. Defaulting to landscape.");
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          break;
      }
    }
    return orientation;
  }

  public static boolean verifyNickname(String nickName) {
    return checkLength(nickName.length(), Const.MIN_NICKNAME_LENGTH, Const.MAX_NICKNAME_LENGTH);
  }

  public static boolean verifyPassword(String passWord) {
    return checkLength(passWord.length(), Const.MIN_PASSWORD_LENGTH, Const.MAX_PASSWORD_LENGTH);
  }

  public static boolean checkLength(int length, int min, int max) {
    return length >= min && length <= max;
  }

  public static String buildImage(String url) {
    return buildImage(url, true);
  }

  public static String buildImage(String url, boolean large) {
    if (TextUtils.isEmpty(url)) {
      return null;
    }
    return url.replace("{size}", large ? "large" : "small").replace("{ext}", "webp");
  }

  public static byte[] toByteArray(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024 * 4];
    int n = 0;
    while ((n = in.read(buffer)) != -1) {
      out.write(buffer, 0, n);
    }
    return out.toByteArray();
  }

  public static String buildImage(@DrawableRes int drawable) {
    return new Uri.Builder().scheme("res").path(String.valueOf(drawable)).build().toString();
  }

  public static Drawable setDrawableBounds(Drawable drawable) {
    if (drawable == null) {
      return null;
    }
    int h = drawable.getIntrinsicHeight();
    int w = drawable.getIntrinsicWidth();
    drawable.setBounds(0, 0, w, h);
    return drawable;
  }

  public static void setEditTextContent(EditText editText, String content) {
    editText.setText(content);
    if (!TextUtils.isEmpty(content)) {
      Editable text = editText.getText();
      Selection.setSelection(text, text.length());
    }
  }
}