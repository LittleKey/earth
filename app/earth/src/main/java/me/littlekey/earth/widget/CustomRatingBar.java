package me.littlekey.earth.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.RatingBar;

import me.littlekey.earth.R;
import me.littlekey.earth.utils.ResourceUtils;

/**
 * Created by littlekey on 16/6/16.
 */
public class CustomRatingBar extends RatingBar {

  public CustomRatingBar(Context context) {
    this(context, null);
  }

  public CustomRatingBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CustomRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRatingBar);
    try {
      @ColorInt int emptyColor = a.getColor(R.styleable.CustomRatingBar_emptyColor,
          ResourceUtils.getColor(R.color.gray));
      @ColorInt int halfColor = a.getColor(R.styleable.CustomRatingBar_halfColor,
          ResourceUtils.getColor(R.color.white));
      @ColorInt int fullColor = a.getColor(R.styleable.CustomRatingBar_fullColor,
          ResourceUtils.getColor(R.color.yellow));
      setDrawableColor(emptyColor, halfColor, fullColor);
    } finally {
      a.recycle();
    }
  }

  public void setDrawableColor(@ColorInt int emptyColor, @ColorInt int halfColor, @ColorInt int fullColor) {
    LayerDrawable star = (LayerDrawable) getProgressDrawable();
    star.getDrawable(0).setColorFilter(emptyColor, PorterDuff.Mode.SRC_ATOP);
    star.getDrawable(1).setColorFilter(halfColor, PorterDuff.Mode.SRC_ATOP);
    star.getDrawable(2).setColorFilter(fullColor, PorterDuff.Mode.SRC_ATOP);
  }
}
