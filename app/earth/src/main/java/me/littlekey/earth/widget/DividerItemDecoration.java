package me.littlekey.earth.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by littlekey on 16/6/10.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

  private Drawable mDivider;
  private int mPadding;

  public DividerItemDecoration(Drawable divider, int padding) {
    mDivider = divider;
    mPadding = padding;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    if (mDivider == null || parent.getChildAdapterPosition(view) < 1) {
      return;
    }
    if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
      outRect.top = mDivider.getIntrinsicHeight();
    } else {
      outRect.left = mDivider.getIntrinsicWidth();
    }
  }

  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    if (mDivider == null) {
      super.onDrawOver(c, parent, state);
      return;
    }
    int left = 0, right = 0, top = 0, bottom = 0, size;
    int orientation = getOrientation(parent);
    int childCount = parent.getChildCount();

    if (orientation == LinearLayoutManager.VERTICAL) {
      size = mDivider.getIntrinsicHeight();
      left = parent.getPaddingLeft();
      right = parent.getWidth() - parent.getPaddingRight();
      left += mPadding;
      right -= mPadding;
    } else {
      size = mDivider.getIntrinsicWidth();
      top = parent.getPaddingTop();
      bottom = parent.getHeight() - parent.getPaddingBottom();
      top += mPadding;
      bottom -= mPadding;
    }

    for (int i = 1; i < childCount; ++i) {
      View child = parent.getChildAt(i);
      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

      if (orientation == LinearLayoutManager.VERTICAL) {
        top = child.getTop() - params.topMargin;
        bottom = top + size;
      } else {
        left = child.getLeft() - params.leftMargin;
        right = left + size;
      }
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
      // NOTE : draw ebd line
      if (i == childCount - 1) {
        top =  child.getBottom() + params.bottomMargin;
        bottom = top + size;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
      }
    }
  }

  private int getOrientation(RecyclerView parent) {
    if (parent.getLayoutManager() instanceof LinearLayoutManager) {
      return ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
    } else {
      throw new IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.");
    }
  }
}
