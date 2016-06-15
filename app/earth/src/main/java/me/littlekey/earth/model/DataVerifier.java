package me.littlekey.earth.model;

import android.text.TextUtils;

import com.squareup.wire.Wire;

import org.jsoup.nodes.Element;

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
    if (TextUtils.isEmpty(user.display_name)) {
      return null;
    }
    User.Builder builder = user.newBuilder();
    builder//.image(Wire.get(user.image, VitalityUtils.buildImage(R.drawable.avatar_default)))
        .display_name(Wire.get(user.display_name, Const.EMPTY_STRING));
    return builder.build();
  }

  public static Element verify(Element element) {
    if (element == null) {
      return null;
    }
    if (element.children().size() != 4) {
      return null;
    }
    // TODO
    return element;
  }
}