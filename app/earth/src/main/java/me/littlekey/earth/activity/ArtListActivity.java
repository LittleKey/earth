package me.littlekey.earth.activity;

import android.content.Intent;
import android.view.Gravity;

import me.littlekey.earth.fragment.ArtListFragment;
import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.utils.NavigationManager;

/**
 * Created by littlekey on 16/6/11.
 */
public class ArtListActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return ArtListFragment.newInstance(NavigationManager.parseIntent(intent));
  }

  @Override
  protected boolean hasToolbar() {
    return false;
  }

  @Override
  protected boolean hasBackBtn() {
    return false;
  }

  @Override
  @SuppressWarnings("RtlHardcoded")
  public void onBackPressed() {
    if (getFragment() instanceof ArtListFragment
        && (((ArtListFragment) getFragment()).isDrawerOpen(Gravity.LEFT)
            || ((ArtListFragment) getFragment()).isDrawerOpen(Gravity.RIGHT))) {
      ((ArtListFragment) getFragment()).closeDrawers();
      return;
    }
    super.onBackPressed();
  }
}
