package me.littlekey.earth.model;

import me.littlekey.network.NameValuePair;

import java.util.List;

import me.littlekey.earth.model.data.ArtDetailDataGenerator;
import me.littlekey.earth.model.data.ArtListsDataGenerator;
import me.littlekey.earth.model.data.EarthDataGenerator;
import me.littlekey.earth.model.data.LikedDataGenerator;
import me.littlekey.earth.network.ApiType;

/**
 * Created by littlekey on 16/6/10.
 */
public class DataGeneratorFactory {
  private DataGeneratorFactory() {}

  public static EarthDataGenerator<?>
  createDataGenerator(ApiType apiType, List<String> paths, NameValuePair... pairs) {
    switch (apiType) {
      case TAG_LIST:
      case HOME_LIST:
      case SEARCH_LIST:
      case SEARCH_FAV_LIST:
      case FAV_LIST:
        return new ArtListsDataGenerator(apiType, paths, pairs);
      case ART_DETAIL:
        return new ArtDetailDataGenerator(paths, pairs);
      case LIKED:
        return new LikedDataGenerator(pairs);
      default:
        throw new IllegalStateException("Unknown api type:" + apiType.name());
    }
  }
}
