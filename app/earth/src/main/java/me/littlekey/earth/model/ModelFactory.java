package me.littlekey.earth.model;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.model.proto.User;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;

/**
 * Created by littlekey on 16/6/10.
 */

public class ModelFactory {
  private ModelFactory() {
  }

  public static Model createModelFromUser(User user, Model.Template template) {
    user = DataVerifier.verify(user);
    if (user == null) {
      return null;
    }
    Flag flag = new Flag.Builder()
        .is_me(EarthApplication.getInstance().getAccountManager().isSelf(user.user_id))
        .build();
    Model.Builder builder = new Model.Builder()
        .type(Model.Type.USER)
        .template(template)
        .identity(user.user_id)
        .title(user.display_name)
        .cover(EarthUtils.buildImage(user.image))
        .description(user.bio)
        .flag(flag)
        .user(user);
    Map<Integer, Action> actions = new HashMap<>();
    Bundle bundle = new Bundle();
    bundle.putString(Const.EXTRA_IDENTITY, user.user_id);
    bundle.putParcelable(Const.KEY_MODEL, builder.build());
    actions.put(Const.ACTION_MAIN, new Action.Builder()
        .type(Action.Type.SHOW_USER_CARD)
        .bundle(bundle)
        .build());
    return builder.actions(actions).build();
  }

  public static Model createHeaderModel(String title) {
    return new Model.Builder().template(Model.Template.HEADER).title(title).build();
  }

  public static Model createDefaultModel(String title, String avatar, Model.Template template,
                                         Class clazz, Bundle bundle) {
    Map<Integer, Action> actions = new HashMap<>();
    if (clazz != null) {
      actions.put(Const.ACTION_MAIN, new Action.Builder()
          .type(Action.Type.JUMP)
          .bundle(bundle)
          .clazz(clazz).build());
    }
    return new Model.Builder().template(template).title(title).cover(avatar).actions(actions)
        .build();
  }
}