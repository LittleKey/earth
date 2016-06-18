package me.littlekey.earth.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import me.littlekey.earth.widget.DetailTransition;

/**
 * Created by littlekey on 16/6/10.
 */


public abstract class BaseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setSharedElementEnterTransition(new DetailTransition(500, 100));
      getWindow().setSharedElementReturnTransition(new DetailTransition(500, 100));
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
}
