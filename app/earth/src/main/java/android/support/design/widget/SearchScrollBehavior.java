package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import java.util.List;

import me.littlekey.earth.R;
import me.littlekey.earth.utils.Const;

/**
 * Created by littlekey on 16/6/21.
 * TODO : code refactoring
 * {@link android.support.design.widget.SearchAppBarBehavior}
 * {@link Const#ART_LIST_TOP_PADDING}
 */
@SuppressWarnings("unused")
public class SearchScrollBehavior extends AppBarLayout.ScrollingViewBehavior {

  private final Rect mTempRect1 = new Rect();
  private final Rect mTempRect2 = new Rect();

  private RecyclerView mRecyclerView;
  private Integer mLast = null;
  private int mScrollPaddingTop;

  public SearchScrollBehavior() {}

  public SearchScrollBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchScrollBehavior);
    try {
      mScrollPaddingTop = a.getDimensionPixelSize(R.styleable.SearchScrollBehavior_scrollPaddingTop, 0);
    } finally {
      a.recycle();
    }
  }

  @Override
  public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
    boolean rlt = false;
    if (mRecyclerView == null) {
      mRecyclerView = (RecyclerView) child.findViewById(R.id.recycler);
    }
    if (mRecyclerView.computeVerticalScrollOffset() < mScrollPaddingTop) {
      final CoordinatorLayout.Behavior behavior =
          ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
      if (behavior instanceof AppBarLayout.Behavior) {
        // Offset the child, pinning it to the bottom the header-dependency, maintaining
        // any vertical gap, and overlap
        final AppBarLayout.Behavior ablBehavior = (AppBarLayout.Behavior) behavior;
        int offset = dependency.getBottom() - child.getTop()
            + ablBehavior.getTopBottomOffsetForScrollingSibling() - ablBehavior.getTopAndBottomOffset()
            + getVerticalLayoutGap()
            - getOverlapPixelsForOffset(dependency);
        if (mLast != null) {
          mRecyclerView.setPadding(
              mRecyclerView.getPaddingLeft(),
              Math.max(0, Math.min(mScrollPaddingTop,
                  mRecyclerView.getPaddingTop() + offset - mLast)),
              mRecyclerView.getPaddingRight(),
              mRecyclerView.getPaddingBottom());
          rlt = true;
        }
        mLast = offset;
        return rlt;
      }
    }
    mLast = null;
    return false;
  }

  @Override
  protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
    final List<View> dependencies = parent.getDependencies(child);
    final View header = findFirstDependency(dependencies);

    if (header != null) {
      final CoordinatorLayout.LayoutParams lp =
          (CoordinatorLayout.LayoutParams) child.getLayoutParams();
      final Rect available = mTempRect1;
      available.set(parent.getPaddingLeft() + lp.leftMargin,
          parent.getPaddingTop() + lp.topMargin,
          parent.getWidth() - parent.getPaddingRight() - lp.rightMargin,
          parent.getHeight() - parent.getPaddingBottom() - lp.bottomMargin);

      final Rect out = mTempRect2;
      GravityCompat.apply(resolveGravity(lp.gravity), child.getMeasuredWidth(),
          child.getMeasuredHeight(), available, out, layoutDirection);

      final int overlap = getOverlapPixelsForOffset(header);

      child.layout(out.left, out.top - overlap, out.right, out.bottom - overlap);
    } else {
      // If we don't have a dependency, let super handle it
      super.layoutChild(parent, child, layoutDirection);
    }
  }

  private static int resolveGravity(int gravity) {
    return gravity == Gravity.NO_GRAVITY ? GravityCompat.START | Gravity.TOP : gravity;
  }
}
