package me.littlekey.earth.event;

import java.util.List;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/23.
 */
public class OnLoadedCommentsEvent {

  private String gid;
  private List<Model> comments;

  public OnLoadedCommentsEvent(String gid, List<Model> comments) {
    this.comments = comments;
    this.gid = gid;
  }

  public List<Model> getComments() {
    return comments;
  }

  public String getIdentity() {
    return gid;
  }
}
