package me.littlekey.earth.activity;

import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.fragment.HomeFragment;
import me.littlekey.earth.utils.NavigationManager;

/**
 * Created by littlekey on 16/6/11.
 */
public class HomeActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment() {
    return HomeFragment.newInstance(NavigationManager.parseIntent(getIntent()));
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
