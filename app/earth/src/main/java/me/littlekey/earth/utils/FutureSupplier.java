package me.littlekey.earth.utils;

import android.support.annotation.Nullable;

import java.util.concurrent.Future;

/**
 * Created by littlekey on 16/6/30.
 */
public interface FutureSupplier<F extends Future<?>> {

  F get(@Nullable String token, String gid, int position);
}
