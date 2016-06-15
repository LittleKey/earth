package me.littlekey.earth.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.RatingBar;

import me.littlekey.earth.R;

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
    LayerDrawable star = (LayerDrawable) getProgressDrawable();
    star.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
    star.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
  }
}
