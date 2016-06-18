package me.littlekey.earth.model;

import android.os.Bundle;

import com.yuanqi.network.NameValuePair;

import me.littlekey.earth.model.data.ArtDataGenerator;
import me.littlekey.earth.model.data.EarthDataGenerator;
import me.littlekey.earth.model.data.ArtListsDataGenerator;
import me.littlekey.earth.network.ApiType;

/**
 * Created by littlekey on 16/6/10.
 */
public class DataGeneratorFactory {
  private DataGeneratorFactory() {}

  public static EarthDataGenerator<?>
  createDataGenerator(ApiType apiType, Bundle bundle, NameValuePair... pairs) {
    switch (apiType) {
      case TAG_LIST:
      case ART_LIST:
        return new ArtListsDataGenerator(apiType, bundle, pairs);
      case ART_VIEWER:
        return new ArtDataGenerator(bundle, pairs);
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
  }
}
