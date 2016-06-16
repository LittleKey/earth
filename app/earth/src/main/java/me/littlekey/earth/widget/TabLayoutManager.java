package me.littlekey.earth.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by littlekey on 16/6/18.
 */
public class TabLayoutManager extends LinearLayoutManager {

  private Context mContext;

  public TabLayoutManager(Context context) {
    super(context, HORIZONTAL, false);
    mContext = context;
  }
  @Override
  public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
      int position) {
    LinearSmoothScroller smoothScroller =
        new LinearSmoothScroller(mContext) {

          @Override
          protected int getHorizontalSnapPreference() {
            return SNAP_TO_START;
          }

          @Override
          public int calculateDxToMakeVisible(View view, int snapPreference) {
            // scroll view in center of RecyclerView
            return super.calculateDxToMakeVisible(view, snapPreference)
                + (getWidth() - view.getWidth()) / 2;
          }

          @Override
          public PointF computeScrollVectorForPosition(int targetPosition) {
            return TabLayoutManager.this.computeScrollVectorForPosition(targetPosition);
          }
        };
    smoothScroller.setTargetPosition(position);
    startSmoothScroll(smoothScroller);
  }
}
