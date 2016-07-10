package me.littlekey.earth.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;

import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.fragment.ArtListFragment;
import me.littlekey.earth.utils.NavigationManager;

/**
 * Created by littlekey on 16/6/11.
 */
public class ArtListActivity extends SingleFragmentActivity {

  private GestureDetector mGestureDetector;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mGestureDetector = new GestureDetector(this,
        new GestureDetector.SimpleOnGestureListener() {
          @Override
          @SuppressWarnings("RtlHardcoded")
          public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (getFragment() instanceof ArtListFragment
                && Math.abs(velocityY) < Math.abs(velocityX)) {
              if (velocityX > 0) {
                if (((ArtListFragment) getFragment()).isDrawerOpen(Gravity.RIGHT)) {
                  ((ArtListFragment) getFragment()).closeDrawer(Gravity.RIGHT);
                } else {
                  ((ArtListFragment) getFragment()).openDrawer(Gravity.LEFT);
                }
              } else {
                if (((ArtListFragment) getFragment()).isDrawerOpen(Gravity.LEFT)) {
                  ((ArtListFragment) getFragment()).closeDrawer(Gravity.LEFT);
                } else {
                  ((ArtListFragment) getFragment()).openDrawer(Gravity.RIGHT);
                }
              }
              return true;
            }
            return false;
          }
        });
  }

  @Override
  protected BaseFragment createFragment() {
    return ArtListFragment.newInstance(NavigationManager.parseIntent(getIntent()));
  }

  @Override
  protected boolean hasToolbar() {
    return false;
  }

  @Override
  protected boolean hasBackBtn() {
    return false;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return (mGestureDetector != null && mGestureDetector.onTouchEvent(ev))
        || super.dispatchTouchEvent(ev);
  }
}
