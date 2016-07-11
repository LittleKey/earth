package me.littlekey.earth.activity;

import android.content.Intent;

import me.littlekey.earth.R;
import me.littlekey.earth.fragment.BaseFragment;
import me.littlekey.earth.fragment.DownloadFragment;
import me.littlekey.earth.utils.EarthUtils;

/**
 * Created by littlekey on 16/7/5.
 */
public class DownloadActivity extends SingleFragmentActivity {

  @Override
  protected BaseFragment createFragment(Intent intent) {
    return DownloadFragment.newInstance();
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
    return EarthUtils.formatString(R.string.download_title);
  }
}
