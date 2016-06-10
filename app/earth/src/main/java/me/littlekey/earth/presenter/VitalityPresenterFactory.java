package me.littlekey.earth.presenter;

import android.view.ViewGroup;

import com.yuanqi.mvp.presenter.ViewGroupPresenter;

/**
 * Use this factory to build Presenter, so that you can make all change in one class.<br/>
 * Created by nengxiangzhou on 15/5/8.
 */
public class VitalityPresenterFactory {
  private VitalityPresenterFactory() {}

  public static ViewGroupPresenter createEmptyPresenter(ViewGroup parent, int layout) {
    return new ViewGroupPresenter(parent, layout)
        .add(new ActionPresenter());
  }
}
