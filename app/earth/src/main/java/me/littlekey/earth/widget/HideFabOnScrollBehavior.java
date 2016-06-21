package me.littlekey.earth.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by littlekey on 16/6/21.
 */public class HideFabOnScrollBehavior extends FloatingActionButton.Behavior {

  public HideFabOnScrollBehavior(Context context) {
    this(context, null);
  }

  public HideFabOnScrollBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

    if (child.isShown() && dyConsumed > 0) {
      child.hide();
    } else if (!child.isShown() && dyConsumed < 0) {
      child.show();
    }
  }

  @Override
  public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
  }
}