package me.littlekey.earth.utils;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by littlekey on 16/6/30.
 */
public interface TokenSupplier {

  @Nullable String get(String gid, int position);

  void insertPage(String gid, int page, List<String> tokens);

  void insertPosition(String gid, int position, String... tokens);
}
