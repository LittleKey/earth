package android.support.design.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by littlekey on 16/6/19.
 */
public class FixedFlingBehavior extends AppBarLayout.Behavior {

  private boolean isPositive;

  public FixedFlingBehavior() {

  }

  public FixedFlingBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
    if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
      velocityY = velocityY * -1;
    }
    return velocityY > 0 && child.getBottom() > 0
        && onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, false);
  }

  @Override
  public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
    if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
      velocityY = velocityY * -1;
    }
    if (target instanceof SwipeRefreshLayout && velocityY < 0) {
      target = ((SwipeRefreshLayout) target).getChildAt(0);
    }
    if (target instanceof RecyclerView && velocityY < 0) {
      RecyclerView recyclerView = (RecyclerView) target;
      consumed = recyclerView.computeVerticalScrollOffset() > 0;
    }
    return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
  }

  @Override
  public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    isPositive = dy > 0;
  }
}
