package me.littlekey.earth.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by littlekey on 16/7/1.
 */
public class PictureController extends FrameLayout {

  public PictureController(Context context) {
    this(context, null);
  }

  public PictureController(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PictureController(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}
