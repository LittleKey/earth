package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import me.littlekey.earth.R;
import me.littlekey.earth.utils.Const;


/**
 * Created by littlekey on 16/6/22.
 * TODO : code refactoring
 * {@link android.support.design.widget.SearchScrollBehavior}
 * {@link Const#ART_LIST_TOP_PADDING}
 */
public class SearchAppBarBehavior extends AppBarLayout.Behavior {

  private static final int INVALID_SCROLL_RANGE = -1;

  private boolean mSkipNestedPreScroll;
  private int mDownPreScrollRange = INVALID_SCROLL_RANGE;

  private boolean isPositive;
  private int mScrollPaddingTop;

  public SearchAppBarBehavior() {
  }

  public SearchAppBarBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchAppBarBehavior);
    try {
      mScrollPaddingTop = a.getDimensionPixelSize(R.styleable.SearchAppBarBehavior_appbarPaddingTop, 0);
    } finally {
      a.recycle();
    }
  }

  @Override
  public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
      View target, int dx, int dy, int[] consumed) {
    isPositive = dy > 0;
    if (dy != 0 && !mSkipNestedPreScroll) {
      int min, max;
      min = -child.getTotalScrollRange();
      max = dy < 0 ? min + getDownNestedPreScrollRange(child) : 0;
      scroll(coordinatorLayout, child, dy, min, max);
    }
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
      View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    mSkipNestedPreScroll = dyUnconsumed < 0;
    if (target instanceof RecyclerView) {
      // NOTE : major dy. i don't know why, but it's work fine.
      int dy = dyUnconsumed < 0 ? dyUnconsumed + dyConsumed : dyUnconsumed;
      if (((RecyclerView) target).computeVerticalScrollOffset() == 0) {
        if (target.getPaddingTop() > 0 || dy < 0) {
          target.setPadding(target.getPaddingLeft(),
              Math.max(0, Math.min(mScrollPaddingTop, target.getPaddingTop() - dy)),
              target.getPaddingRight(),
              target.getPaddingBottom());
          // NOTE : when scroll down (dy < 0) do not consume Y
          dyUnconsumed = dy <= 0 ? dyUnconsumed : 0;
        }
      }
    }
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
  }

  @Override
  public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
      float velocityX, float velocityY, boolean consumed) {
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
    if (consumed) {
      if (target.getPaddingTop() > 0 || velocityY < 0) {
        target.setPadding(target.getPaddingLeft(),
            (int) Math.max(0, Math.min(mScrollPaddingTop, target.getPaddingTop() - velocityY)),
            target.getPaddingRight(),
            target.getPaddingBottom());
      }
    }
    return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
  }

  private int getDownNestedPreScrollRange(ViewGroup parent) {
    if (mDownPreScrollRange != INVALID_SCROLL_RANGE) {
      // If we already have a valid value, return it
      return mDownPreScrollRange;
    }

    int range = 0;
    for (int i = parent.getChildCount() - 1; i >= 0; i--) {
      final View child = parent.getChildAt(i);
      final AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) child.getLayoutParams();
      final int childHeight = child.getMeasuredHeight();
      final int flags = lp.mScrollFlags;

      if ((flags & AppBarLayout.LayoutParams.FLAG_QUICK_RETURN) == AppBarLayout.LayoutParams.FLAG_QUICK_RETURN) {
        // First take the margin into account
        range += lp.topMargin + lp.bottomMargin;
        // The view has the quick return flag combination...
        if ((flags & AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED) != 0) {
          // If they're set to enter collapsed, use the minimum height
          range += ViewCompat.getMinimumHeight(child);
        } else if ((flags & AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
          // Only enter by the amount of the collapsed height
          range += childHeight - ViewCompat.getMinimumHeight(child);
        } else {
          // Else use the full height
          range += childHeight;
        }
      } else if (range > 0) {
        // If we've hit an non-quick return scrollable view, and we've already hit a
        // quick return view, return now
        break;
      }
    }
    return mDownPreScrollRange = Math.max(0, range);
  }
}
