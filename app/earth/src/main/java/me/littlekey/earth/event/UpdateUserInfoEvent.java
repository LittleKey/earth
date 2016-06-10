package me.littlekey.earth.event;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/11.
 */
public class UpdateUserInfoEvent {

  private Model mUser;

  public UpdateUserInfoEvent(Model user) {
    mUser = user;
  }

  public Model getUser() {
    return mUser;
  }
}
