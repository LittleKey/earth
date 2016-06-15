package me.littlekey.earth.account;

import android.text.TextUtils;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.event.AccountChangeEvent;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.PreferenceUtils;

/**
 * Created by littlekey on 16/6/11.
 */
public class AccountManager {
  private boolean isSignIn;
  private String mUserId;
  private String mPassHash;

  public void login(String userId, String passHash) {
    setIsSignIn(true);
    setUser(userId, passHash);
    EventBus.getDefault().post(new AccountChangeEvent(true));
  }

  public void logout() {
    setIsSignIn(false);
    mUserId = null;
    mPassHash = null;
    EventBus.getDefault().post(new AccountChangeEvent(false));
    PreferenceUtils.removeString(Const.LAST_ACTION, Const.LAST_USER_ID);
    PreferenceUtils.removeString(Const.LAST_ACTION, Const.LAST_PASS_HASH);
  }

  public boolean isSignIn() {
    return isSignIn;
  }

  private void setIsSignIn(boolean isSignIn) {
    this.isSignIn = isSignIn;
  }

  public String getUserId() {
    return mUserId;
  }

  public String getPassHash() {
    return mPassHash;
  }

  private boolean setUser(String userId, String passHash) {
    return setUserId(userId) | setPassHash(passHash);
  }

  private boolean setUserId(String userId) {
    if (!TextUtils.isEmpty(userId) && !userId.equals(mUserId)) {
      mUserId = userId;
      PreferenceUtils.setString(Const.LAST_ACTION, Const.LAST_USER_ID, userId);
      return true;
    }
    return false;
  }

  private boolean setPassHash(String passHash) {
    if (!TextUtils.isEmpty(passHash) && !passHash.equals(mPassHash)) {
      mPassHash = passHash;
      PreferenceUtils.setString(Const.LAST_ACTION, Const.LAST_PASS_HASH, passHash);
      return true;
    }
    return false;
  }

  public boolean isSelf(String id) {
    return isSignIn() && mUserId != null && TextUtils.equals(mUserId, id);
  }
}
