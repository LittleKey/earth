package me.littlekey.earth.model;

import android.text.TextUtils;

import com.squareup.wire.Wire;

import java.util.ArrayList;
import java.util.List;

import me.littlekey.earth.R;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Fav;
import me.littlekey.earth.model.proto.Image;
import me.littlekey.earth.model.proto.Tag;
import me.littlekey.earth.model.proto.User;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;

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

  public static Art verify(Art art) {
    if (art == null) {
      return null;
    }
    if (TextUtils.isEmpty(art.gid)
        || TextUtils.isEmpty(art.token)
        || TextUtils.isEmpty(art.title)) {
      return null;
    }
    Count count = verify(Wire.get(art.count, new Count.Builder().build()));
    if (count == null) {
      return null;
    }
    List<Tag> tags = Wire.get(art.tags, new ArrayList<Tag>());
    return art.newBuilder()
        .publisher_name(Wire.get(art.publisher_name, Const.EMPTY_STRING))
        .cover(Wire.get(art.cover, Const.EMPTY_STRING))
        .count(count)
        .category(Wire.get(art.category, Model.Category.UNKNOWN.getValue()))
        .date(Wire.get(art.date, Const.EMPTY_STRING))
        .url(Wire.get(art.url, Const.EMPTY_STRING))
        .language(Wire.get(art.language, Const.EMPTY_STRING))
        .file_size(Wire.get(art.file_size, Const.EMPTY_STRING))
        .liked(Wire.get(art.liked, false))
        .tags(tags)
        .build();
  }

  public static Count verify(Count count) {
    if (count == null) {
      return null;
    }
    return count.newBuilder()
        .rating(Wire.get(count.rating, 0f))
        .pages(Wire.get(count.pages, 0))
        .likes(Wire.get(count.likes, 0))
        .rating_count(Wire.get(count.rating_count, 0))
        .build();
  }

  public static Tag verify(Tag tag) {
    if (tag == null) {
      return null;
    }
    if (TextUtils.isEmpty(tag.id)) {
      return null;
    }
    return tag.newBuilder()
        .key(Wire.get(tag.key, Const.EMPTY_STRING))
        .values(Wire.get(tag.values, new ArrayList<Tag>()))
        .build();
  }

  public static Image verify(Image image) {
    if (image == null) {
      return null;
    }
    if (TextUtils.isEmpty(image.src)) {
      return null;
    }
    return image.newBuilder()
        .origin_url(Wire.get(image.origin_url, Const.EMPTY_STRING))
        .offset(Wire.get(image.offset, 0))
        .build();
  }

  public static Fav verify(Fav fav) {
    if (fav == null) {
      return null;
    }
    if (TextUtils.isEmpty(fav.id)) {
      return null;
    }
    return fav.newBuilder()
        .name(Wire.get(fav.name, EarthUtils.formatString(R.string.default_format_name, fav.id)))
        .build();
  }
}