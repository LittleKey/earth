package me.littlekey.earth.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.yuanqi.mvp.widget.MvpSwipeRefreshLayout;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/10.
 */
public class EarthSwipeRefreshLayout extends MvpSwipeRefreshLayout<Model> {

  public static final int DEFAULT_CIRCLE_TARGET = 64;

  public EarthSwipeRefreshLayout(Context context) {
    this(context, null);
  }

  public EarthSwipeRefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
}

