package me.littlekey.earth.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by littlekey on 16/6/19.
 */
public class FixedFlingBehavior extends AppBarLayout.Behavior {

  public FixedFlingBehavior() {

  }

  public FixedFlingBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
    if (target instanceof SwipeRefreshLayout && velocityY < 0) {
      target = ((SwipeRefreshLayout) target).getChildAt(0);
    }
    if (target instanceof RecyclerView && velocityY < 0) {
      RecyclerView recyclerView = (RecyclerView) target;
      consumed = velocityY > 0 || recyclerView.computeVerticalScrollOffset() > 0;
    }
    return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
  }
}
