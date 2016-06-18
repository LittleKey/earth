package me.littlekey.earth.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.TransitionSet;
import android.view.animation.AnticipateOvershootInterpolator;

/**
 * Created by littlekey on 16/6/19.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DetailTransition extends TransitionSet {

  public DetailTransition(int duration, int delay) {
    setOrdering(ORDERING_TOGETHER);
    addTransition(new ChangeBounds())
        .setDuration(duration)
        .setStartDelay(delay)
        .setInterpolator(new AnticipateOvershootInterpolator());
  }
}