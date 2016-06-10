package me.littlekey.earth.event;

/**
 * Created by littlekey on 16/6/11.
 */
public class AccountChangeEvent {
  private boolean mIsSignIn;
  public AccountChangeEvent(boolean mIsSignIn) {
    this.mIsSignIn = mIsSignIn;
  }

  public boolean isSignIn() {
    return mIsSignIn;
  }
}
