package me.littlekey.earth.event;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/7/13.
 */
public class OnQuickSearchEvent {

  private Model model;

  public OnQuickSearchEvent(Model model) {
    this.model = model;
  }

  public Model getModel() {
    return model;
  }
}
