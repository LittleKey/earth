package me.littlekey.earth.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Transition;

import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.fragment.ViewerFragment;
import me.littlekey.earth.utils.NavigationManager;

/**
 * Created by littlekey on 16/6/16.
 */
public class ViewerActivity extends SingleFragmentActivity {

  private ViewerFragment mFragment;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
          mFragment.onTransitionEnd();
        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
      });
    }
  }

  @Override
  protected BaseFragment createFragment() {
    return mFragment = ViewerFragment.newInstance(NavigationManager.parseIntent(getIntent()));
  }

  @Override
  protected boolean hasToolbar() {
    return false;
  }

  @Override
  public void onBackPressed() {
    if (!mFragment.onBackPressed()) {
      super.onBackPressed();
    }
  }
}
