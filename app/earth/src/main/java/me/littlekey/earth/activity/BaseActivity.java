package me.littlekey.earth.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;

import me.littlekey.earth.R;
import me.littlekey.earth.utils.ToastUtils;
import me.littlekey.earth.widget.DetailTransition;

/**
 * Created by littlekey on 16/6/10.
 */


public abstract class BaseActivity extends AppCompatActivity {

  private ExitHandler mExitHandler;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setSharedElementEnterTransition(new DetailTransition(360, 0));
      getWindow().setSharedElementReturnTransition(new DetailTransition(360, 0));
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
//    ZhugeSDK.getInstance().init(getApplicationContext());
//    MobclickAgent.onResume(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
//    MobclickAgent.onPause(this);
//    HuPuMountInterface.onPause(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
//    HuPuMountInterface.onStop(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
//    HuPuMountInterface.onDestroy(this);
  }

  @Override
  public void onBackPressed() {
    if (isTaskRoot()) {
      if (!getSupportFragmentManager().popBackStackImmediate()) {
        if (mExitHandler == null) {
          mExitHandler = new ExitHandler(this);
        }
        mExitHandler.tryExit();
      }
    } else {
      super.onBackPressed();
    }
  }

  // 关闭软键盘
  public void closeKeyboard() {
    View view = getWindow().peekDecorView();
    if (view != null) {
      InputMethodManager inputManager =
          (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  public void showKeyBoard() {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
  }

  private static class ExitHandler extends Handler {

    private final static int MSG_TRY_EXIT = 0;
    private final static int MSG_CANCEL = 1;

    private final static int TIME_OUT = 2 * 1000;

    private WeakReference<BaseActivity> mWeakActivity;
    private boolean mFlag;

    public ExitHandler(BaseActivity activity) {
      super();
      mWeakActivity = new WeakReference<>(activity);
      mFlag = false;
    }

    @Override
    public void handleMessage(Message msg) {
      BaseActivity activity = mWeakActivity.get();
      if (activity == null || activity.isFinishing()) {
        return;
      }
      switch (msg.what) {
        case MSG_TRY_EXIT:
          if (mFlag) {
            activity.finish();
          } else {
            ToastUtils.toast(R.string.more_press_exit);
          }
          mFlag = true;
          sendMessageDelayed(obtainMessage(MSG_CANCEL), TIME_OUT);
          break;
        case MSG_CANCEL:
          mFlag = false;
          break;
      }
    }

    public void tryExit() {
      Message.obtain(this, MSG_TRY_EXIT).sendToTarget();
    }
  }
}
