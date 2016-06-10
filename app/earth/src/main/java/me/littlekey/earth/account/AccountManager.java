package me.littlekey.earth.account;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.event.AccountChangeEvent;
import me.littlekey.earth.event.UpdateUserInfoEvent;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.proto.User;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.PreferenceUtils;

/**
 * Created by littlekey on 16/6/11.
 */
public class AccountManager {
  private boolean isSignIn;
  private String mSessionId;
  private Model mUser;

  public void login() {
    throw new UnsupportedOperationException("not implemented login.");
  }

  public void logout() {
    setIsSignIn(false);
    mUser = null;
    mSessionId = null;
    EventBus.getDefault().post(new AccountChangeEvent(false));
    PreferenceUtils.removeString(Const.LAST_ACTION, Const.LAST_SESSION_ID);
  }

  public boolean isSignIn() {
    return isSignIn;
  }

  private void setIsSignIn(boolean isSignIn) {
    this.isSignIn = isSignIn;
  }

  public String getSessionId() {
    return mSessionId;
  }

  public void setSessionId(@NonNull String sessionId) {
    if (!TextUtils.isEmpty(sessionId) && !sessionId.equals(mSessionId)) {
      this.mSessionId = sessionId;
      PreferenceUtils.setString(Const.LAST_ACTION, Const.LAST_SESSION_ID, sessionId);
    }
  }

  public boolean isSelf(String id) {
    return isSignIn() && mUser != null && String.valueOf(mUser.getIdentity()).equals(id);
  }

  public Model getUser() {
    return mUser;
  }

  public void setUser(User user) {
    if (user != null && isSelf(user.user_id)) {
      mUser = ModelFactory.createModelFromUser(user, Model.Template.DATA);
      EventBus.getDefault().post(new UpdateUserInfoEvent(mUser));
    }
  }
}
