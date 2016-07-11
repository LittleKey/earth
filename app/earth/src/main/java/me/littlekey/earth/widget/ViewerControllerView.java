package me.littlekey.earth.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import java.lang.ref.WeakReference;

import me.littlekey.earth.R;

/**
 * Created by littlekey on 16/7/1.
 */
public class ViewerControllerView extends FrameLayout
    implements
      GestureDetector.OnGestureListener,
      SeekBar.OnSeekBarChangeListener,
      ViewPager.OnPageChangeListener,
      ViewPager.OnAdapterChangeListener {

  public final static int MSG_SHOW_BAR = 1;
  public final static int MSG_HIDE_BAR = 2;
  public final static int SECOND = 1000;
  private final static int DEFAULT_TIMEOUT = 5 * SECOND;

  private ViewPager mViewer;
  private FrameLayout mControlFrame;
  private ViewerTitleBar mTitleBar;
  private ViewerControlBar mControlBar;

  private ViewerControllerHandler mControllerHandler;
  private GestureDetector mGestureDetector;
  private boolean mShowing = false;
  private int mCurrentPage = -1;

  public ViewerControllerView(Context context) {
    this(context, null);
  }

  public ViewerControllerView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ViewerControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mControllerHandler = new ViewerControllerHandler(this);
    mGestureDetector = new GestureDetector(context, this);
    mGestureDetector.setIsLongpressEnabled(false);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mControlFrame = (FrameLayout) findViewById(R.id.viewer_controller_frame);
    mTitleBar = (ViewerTitleBar) findViewById(R.id.viewer_title_bar);
    mControlBar = (ViewerControlBar) findViewById(R.id.viewer_control_bar);
    mControlBar.setSeekBarChangeListener(this);
  }

  @Override
  public void setFitsSystemWindows(boolean fitSystemWindows) {
    super.setFitsSystemWindows(fitSystemWindows);
    if (fitSystemWindows) {
      setSystemUiVisibility(0);
      mControlFrame.setPadding(0, 0, 0, 0);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      if (mShowing) {
        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      } else {
        int newUiOptions = getSystemUiVisibility();
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        setSystemUiVisibility(newUiOptions);
      }
    }
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return mGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
  }

  @Override
  public boolean onDown(MotionEvent e) {
    return false;
  }

  @Override
  public void onShowPress(MotionEvent e) {

  }

  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    if (mShowing) {
      hide();
    } else {
      show();
    }
    return false;
  }

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    return false;
  }

  @Override
  public void onLongPress(MotionEvent e) {

  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    hide();
    return false;
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    // do nth.
    mControlBar.setCurrentPage(progress);
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    show(0);
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    show();
    if (mViewer != null) {
      mViewer.setCurrentItem(seekBar.getProgress());
    }
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    if (positionOffset == 0) {
      mCurrentPage = position;
    }
  }

  @Override
  public void onPageSelected(int position) {
    if (position != mCurrentPage) {
      mControlBar.setCurrentPage(mCurrentPage = position);
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (state == ViewPager.SCROLL_STATE_IDLE) {
      mControlBar.setCurrentPage(mCurrentPage);
    }
  }

  @Override
  public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
    if (oldAdapter != null) {
      oldAdapter.unregisterDataSetObserver(mDataObserver);
    }
    if (newAdapter != null) {
      newAdapter.registerDataSetObserver(mDataObserver);
      mControlBar.setTotalPage(newAdapter.getCount());
      mControlBar.setCurrentPage(mCurrentPage = viewPager.getCurrentItem());
    }
  }

  private final DataSetObserver mDataObserver = new DataSetObserver() {
    @Override
    public void onChanged() {
      mControlBar.setTotalPage(mViewer.getAdapter().getCount());
      mControlBar.setCurrentPage(mCurrentPage = mViewer.getCurrentItem());
    }

    @Override
    public void onInvalidated() {
      mControlBar.setTotalPage(0);
      mControlBar.setCurrentPage(mCurrentPage = -1);
    }
  };

  public void setViewPager(ViewPager viewPager) {
    mViewer = viewPager;
    mViewer.addOnPageChangeListener(this);
    mViewer.addOnAdapterChangeListener(this);
  }

  public void show() {
    show(DEFAULT_TIMEOUT);
  }

  private void show(int timeout) {
    if (!mShowing) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !getFitsSystemWindows()) {
        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      } else {
        setSystemUiVisibility(0);
      }
      if (mTitleBar != null) {
        mTitleBar.show();
      }
      if (mControlBar != null) {
        mControlBar.show();
      }
      mShowing = true;
    }
    mControllerHandler.removeMessages(MSG_HIDE_BAR);
    if (timeout != 0) {
      mControllerHandler.sendMessageDelayed(
          Message.obtain(mControllerHandler, MSG_HIDE_BAR), timeout);
    }
  }

  private void hide() {
    mControllerHandler.removeMessages(MSG_HIDE_BAR);
    if (mShowing) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !getFitsSystemWindows()) {
        int newUiOptions = getSystemUiVisibility();
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        setSystemUiVisibility(newUiOptions);
      } else {
        setSystemUiVisibility(0);
      }
      if (mTitleBar != null) {
        mTitleBar.hide();
      }
      if (mControlBar != null) {
        mControlBar.hide();
      }
      mShowing = false;
    }
  }

  private static class ViewerControllerHandler extends Handler {

    private WeakReference<ViewerControllerView> mWeakController;

    public ViewerControllerHandler(ViewerControllerView controller) {
      super();
      mWeakController = new WeakReference<>(controller);
    }

    @Override
    public void handleMessage(Message msg) {
      ViewerControllerView controller = mWeakController.get();
      if (controller == null) {
        return;
      }
      switch (msg.what) {
        case MSG_HIDE_BAR:
          controller.hide();
          break;
        case MSG_SHOW_BAR:
          if (msg.obj != null && msg.obj instanceof Integer) {
            controller.show((int) msg.obj);
          } else {
            controller.show();
          }
          break;
      }
    }
  }
}
