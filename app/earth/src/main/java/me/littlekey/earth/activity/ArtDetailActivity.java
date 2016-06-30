package me.littlekey.earth.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.littlekey.earth.fragment.ArtDetailFragment;
import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.utils.NavigationManager;

/**
 * Created by littlekey on 16/6/16.
 */
public class ArtDetailActivity extends SingleFragmentActivity {

  private ArtDetailFragment mFragment;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected BaseFragment createFragment() {
    return mFragment = ArtDetailFragment.newInstance(NavigationManager.parseIntent(getIntent()));
  }

  @Override
  protected boolean hasToolbar() {
    return false;
  }

  @Override
  public void onBackPressed() {
    if (mFragment == null || !mFragment.onBackPressed()) {
      super.onBackPressed();
    }
  }
}
