package me.littlekey.earth.activity;

import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.fragment.ArtListFragment;
import me.littlekey.earth.utils.NavigationManager;

/**
 * Created by littlekey on 16/6/11.
 */
public class ArtListActivity extends SingleFragmentActivity {

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
}
