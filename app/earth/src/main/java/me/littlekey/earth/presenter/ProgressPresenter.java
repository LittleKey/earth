package me.littlekey.earth.presenter;

import android.widget.ProgressBar;

import com.squareup.wire.Wire;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/7/8.
 */
public class ProgressPresenter extends EarthPresenter {

  @Override
  public void bind(Model model) {
    float progress = Wire.get(model.count.progress, 0f);
    bindProgress((ProgressBar) view(), progress);
  }

  private void bindProgress(ProgressBar view, float progress) {
    view.setProgress((int) (progress * 10));
  }
}
