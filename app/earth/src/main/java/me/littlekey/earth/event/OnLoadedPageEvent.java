package me.littlekey.earth.event;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/17.
 */
public class OnLoadedPageEvent {

  private Model model;

  public OnLoadedPageEvent(Model model) {
    this.model = model;
  }

  public Model getModel() {
    return model;
  }
}
