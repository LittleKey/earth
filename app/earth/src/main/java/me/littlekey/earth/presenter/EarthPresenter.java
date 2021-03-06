package me.littlekey.earth.presenter;

import me.littlekey.mvp.presenter.Presenter;

import me.littlekey.earth.model.Model;

/**
 * Created by nengxiangzhou on 15/5/8.
 */
public abstract class EarthPresenter extends Presenter {
  @Override
  public final void bind(Object model) {
    bind((Model) model);
  }

  public abstract void bind(Model model);
}
