package me.littlekey.earth.utils;

import android.content.Context;

import java.util.Set;

import me.littlekey.earth.EarthApplication;

/**
 * Created by littlekey on 16/6/10.
 */
public class PreferenceUtils {
  private PreferenceUtils() {}

  public static String getString(final String name, final String key, final String defaultValue) {
    return EarthApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
        .getString(key, defaultValue);
  }

  public static void setString(final String name, final String key, final String value) {
    EarthApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
        .edit().putString(key, value).commit();
  }

  public static int getInt(final String name, final String key, final int defaultValue) {
    return EarthApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
        .getInt(key, defaultValue);
  }

  public static void setInt(final String name, final String key, final int value) {
    EarthApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
        .edit().putInt(key, value).commit();
  }

  public static boolean getBoolean(final String name, final String key,
                                   final boolean defaultValue) {
    return EarthApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
        .getBoolean(key, defaultValue);
  }

  public static void setBoolean(final String name, final String key, final boolean value) {
    EarthApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
        .edit().putBoolean(key, value).commit();
  }

  public static void removeString(final String name, final String key) {
    EarthApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).edit()
        .remove(key).commit();
  }

  public static void setStringSet(final String name, final String key, final Set<String> value) {
    EarthApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE).edit()
        .putStringSet(key, value).commit();
  }

  public static Set<String> getStringSet(final String name, final String key, final Set<String> defaultValue) {
    return EarthApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
        .getStringSet(key, defaultValue);
  }
}