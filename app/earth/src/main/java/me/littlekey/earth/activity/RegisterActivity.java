package me.littlekey.earth.activity;

import android.content.Intent;

import me.littlekey.earth.R;
import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.fragment.RegisterFragment;

/**
 * Created by littlekey on 16/7/1.
 */
public class RegisterActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return RegisterFragment.newInstance();
  }

  @Override
  protected boolean hasToolbar() {
    return true;
  }

  @Override
  protected boolean hasBackBtn() {
    return true;
  }

  @Override
  protected String activityTitle() {
    return getString(R.string.register);
  }
}
