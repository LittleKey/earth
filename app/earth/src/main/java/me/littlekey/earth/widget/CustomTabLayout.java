package me.littlekey.earth.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by littlekey on 16/6/18.
 */
public class CustomTabLayout extends TabLayout {

  public CustomTabLayout(Context context) {
    this(context, null);
  }

  public CustomTabLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void setupWithViewPager(final @Nullable ViewPager viewPager) {
    post(new Runnable() {
      @Override
      public void run() {
        setupViewPager(viewPager);
      }
    });
  }

  private void setupViewPager(ViewPager viewPager) {
    setOnTabSelectedListener(null);
    super.setupWithViewPager(viewPager);
  }
}
