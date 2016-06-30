package me.littlekey.earth.activity;

import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.fragment.ViewerFragment;
import me.littlekey.earth.utils.NavigationManager;

/**
 * Created by littlekey on 16/6/28.
 */
public class ViewerActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment() {
    return ViewerFragment.newInstance(NavigationManager.parseIntent(getIntent()));
  }

  @Override
  protected boolean hasToolbar() {
    return false;
  }
}
