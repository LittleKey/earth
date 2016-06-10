package me.littlekey.earth.model;

import android.os.Bundle;

import com.yuanqi.network.NameValuePair;

import me.littlekey.earth.model.data.EarthDataGenerator;
import me.littlekey.earth.network.ApiType;

/**
 * Created by littlekey on 16/6/10.
 */
public class DataGeneratorFactory {
  private DataGeneratorFactory() {}

  public static EarthDataGenerator<?>
  createDataGenerator(ApiType apiType, Bundle bundle, NameValuePair... pairs) {
    switch (apiType) {
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
  }
}
