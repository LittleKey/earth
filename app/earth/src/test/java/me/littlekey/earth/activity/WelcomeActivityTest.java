package me.littlekey.earth.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.BuildConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

/**
 * Created by littlekey on 16-9-13.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "AndroidManifest.xml", sdk = Build.VERSION_CODES.LOLLIPOP)
public class WelcomeActivityTest {

  @Before
  public void setUp() {
    // init
  }

  @Test
  public void testCase() {
    WelcomeActivity welcomeActivity = Robolectric.buildActivity(WelcomeActivity.class)
        .create().resume().get();
    Assert.assertNotNull(welcomeActivity);
//    testActivityTurn(welcomeActivity, ArtListActivity.class);
  }

  public void testActivityTurn(BaseActivity firstActivity, Class secondActivity) {
    Intent intent = new Intent(firstActivity.getApplicationContext(), secondActivity);
    Assert.assertEquals(intent, Shadows.shadowOf(firstActivity).getNextStartedActivity());
  }
}
