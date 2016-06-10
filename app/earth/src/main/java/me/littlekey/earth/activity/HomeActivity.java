package me.littlekey.earth.activity;

import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.fragment.HomeFragment;

/**
 * Created by littlekey on 16/6/11.
 */
public class HomeActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment() {
    return HomeFragment.newInstance();
  }

  @Override
  protected boolean hasToolbar() {
    return false;
  }

  @Override
  protected boolean hasBackBtn() {
    return false;
  }
}
