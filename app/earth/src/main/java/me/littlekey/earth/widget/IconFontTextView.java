package me.littlekey.earth.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import me.littlekey.earth.EarthApplication;

/**
 * Created by littlekey on 16/6/13.
 */
public class IconFontTextView extends TextView {
  public IconFontTextView(Context context) {
    this(context, null);
  }

  public IconFontTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public IconFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    Typeface typeface;
    if (!isInEditMode()) {
      typeface = EarthApplication.getInstance().getIconTypeface();
    } else {
      typeface = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
    }
    this.setTypeface(typeface);
  }
}
