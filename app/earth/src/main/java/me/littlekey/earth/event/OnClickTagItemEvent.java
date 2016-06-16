package me.littlekey.earth.event;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/18.
 */
public class OnClickTagItemEvent {

  private Model tag;

  public OnClickTagItemEvent(Model tag) {
    this.tag = tag;
  }

  public Model getTag() {
    return tag;
  }
}
