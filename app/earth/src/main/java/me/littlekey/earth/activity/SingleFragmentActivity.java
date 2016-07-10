package me.littlekey.earth.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import me.littlekey.earth.R;
import me.littlekey.earth.fragment.BaseFragment;

/**
 * Created by littlekey on 16/6/10.
 */
public abstract class SingleFragmentActivity extends BaseActivity {

  private TextView mTitleView;
  private BaseFragment mFragment;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayout());
    if (hasToolbar()) {
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      if (toolbar == null) {
        return;
      }
      setSupportActionBar(toolbar);
      if (hasBackBtn()) {
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
      }
      mTitleView = (TextView) toolbar.findViewById(R.id.title);
      if (!TextUtils.isEmpty(activityTitle())) {
        mTitleView.setText(activityTitle());
      } else {
        mTitleView.setVisibility(View.GONE);
      }
    }
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);
    if (fragment == null) {
      fm.beginTransaction()
          .add(R.id.fragment_container, mFragment = createFragment())
          .commit();
    }
  }

  protected boolean hasToolbar() {
    return true;
  }

  protected boolean hasBackBtn() {
    return true;
  }

  protected String activityTitle() {
    return null;
  }

  protected @LayoutRes int getLayout() {
    return hasToolbar() ? R.layout.activity_single_fragment : R.layout.activity_without_toolbar;
  }

  protected abstract BaseFragment createFragment();

  protected BaseFragment getFragment() {
    return mFragment;
  }
}
