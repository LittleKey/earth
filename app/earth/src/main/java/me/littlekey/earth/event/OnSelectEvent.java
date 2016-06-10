package me.littlekey.earth.event;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/11.
 */
public class OnSelectEvent {

  private boolean select;
  private Model model;

  public OnSelectEvent(boolean select, Model model) {
    this.select = select;
    this.model = model;
  }

  public boolean isSelected() {
    return this.select;
  }

  public Model getModel() {
    return model;
  }
}