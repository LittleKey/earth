package me.littlekey.earth.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.NavigationManager;
import me.littlekey.earth.utils.PreferenceUtils;

/**
 * Created by littlekey on 16/6/13.
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);
    String userId = PreferenceUtils.getString(Const.LAST_ACTION, Const.LAST_USER_ID, null);
    String passHash = PreferenceUtils.getString(Const.LAST_ACTION, Const.LAST_PASS_HASH, null);
    if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(passHash)) {
      EarthApplication.getInstance().getAccountManager().login(userId, passHash);
      NavigationManager.navigationTo(WelcomeActivity.this, HomeActivity.class);
      finish();
    } else {
      TextView btnLogin = (TextView) findViewById(R.id.btn_login);
      if (btnLogin != null) {
        btnLogin.setOnClickListener(this);
        btnLogin.setVisibility(View.VISIBLE);
      }
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_login:
        NavigationManager.navigationTo(WelcomeActivity.this, LoginActivity.class);
        break;
//      case R.id.btn_register:
//        NavigationManager.navigationTo(WelcomeActivity.this, RegisterActivity.class);
//        break;
    }
  }
}
