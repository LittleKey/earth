package me.littlekey.earth.activity;

import android.content.Intent;

import me.littlekey.earth.R;
import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.fragment.LoginFragment;

/**
 * Created by littlekey on 16/6/13.
 */
public class LoginActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return LoginFragment.newInstance();
  }

  @Override
  protected String activityTitle() {
    return getString(R.string.login);
  }
}
