package me.littlekey.earth.event;

/**
 * Created by littlekey on 16/6/20.
 */
public class OnLikedEvent {

  private String gid;
  private boolean liked;

  public OnLikedEvent(String gid, boolean liked) {
    this.gid = gid;
    this.liked = liked;
  }

  public String getIdentity() {
    return gid;
  }

  public boolean isLiked() {
    return liked;
  }
}
