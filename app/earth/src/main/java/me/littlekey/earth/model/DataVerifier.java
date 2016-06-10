package me.littlekey.earth.model;

import android.text.TextUtils;

import com.squareup.wire.Wire;

import me.littlekey.earth.model.proto.User;
import me.littlekey.earth.utils.Const;

/**
 * Created by littlekey on 16/6/10.
 */
public class DataVerifier {
  private DataVerifier() {}

  public static User verify(User user) {
    if (user == null) {
      return null;
    }
    if (TextUtils.isEmpty(user.user_id)) {
      return null;
    }
    User.Builder builder = user.newBuilder();
    builder//.image(Wire.get(user.image, VitalityUtils.buildImage(R.drawable.avatar_default)))
        .display_name(Wire.get(user.display_name, Const.EMPTY_STRING))
        .bio(Wire.get(user.bio, Const.EMPTY_STRING))
        .gender(Wire.get(user.gender, User.Gender.MALE));
    return builder.build();
  }
}