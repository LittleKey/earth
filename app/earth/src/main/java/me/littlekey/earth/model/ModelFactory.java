package me.littlekey.earth.model;

import android.net.Uri;
import android.os.Bundle;

import me.littlekey.base.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.event.OnClickTagItemEvent;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.Comment;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Fav;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.model.proto.Image;
import me.littlekey.earth.model.proto.Picture;
import me.littlekey.earth.model.proto.Tag;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.NavigationManager;

/**
 * Created by littlekey on 16/6/10.
 */

public class ModelFactory {
  private ModelFactory() {
  }

  public static Model createDLCModelFromArt(Art art, Model.Template template) {
    art = DataVerifier.verify(art);
    if (art == null) {
      return null;
    }
    return new Model.Builder()
        .type(Model.Type.DLC)
        .template(template)
        .identity(art.gid)
        .token(art.token)
        .addition(createModelFromArt(art, Model.Template.ITEM_ART))
        .build();
  }

  public static Model createModelFromArt(Art art, Model.Template template) {
    art = DataVerifier.verify(art);
    if (art == null) {
      return null;
    }
    Model.Category category;
    if (art.category >= 0 && art.category < Model.Category.values().length) {
      category = Model.Category.values()[art.category];
    } else {
      throw new IllegalStateException("CATEGORY can not be null.");
    }
    List<Model> subModels = new ArrayList<>();
    for (Tag tag: art.tags) {
      CollectionUtils.add(subModels, createModelFromTag(tag, Model.Template.PARENT_TAG));
    }
    Count count = new Count.Builder()
        .pages(art.pages)
        .rating(art.rating)
        .rating_count(art.rating_count)
        .likes(art.likes)
        .build();
    Model.Builder builder = new Model.Builder()
        .type(Model.Type.ART)
        .template(template)
        .art(art)
        .identity(art.gid)
        .token(art.token)
        .title(art.title)
        .subtitle(art.publisher_name)
        .category(category)
        .date(art.date)
        .url(art.url)
        .flag(new Flag.Builder().is_liked(art.liked).build())
        .language(art.language)
        .fileSize(art.file_size)
        .count(count)
        .cover(art.cover)
        .language(art.language)
        .subModels(subModels);
    Bundle bundle = new Bundle();
    bundle.putParcelable(Const.EXTRA_MODEL, builder.build());
    Map<Integer, Action> actions = new HashMap<>();
    Bundle favBundle = new Bundle();
    favBundle.putString(Const.KEY_GID, art.gid);
    favBundle.putString(Const.KEY_TOKEN, art.token);
    actions.put(Const.ACTION_LIKED, new Action.Builder()
        .type(Action.Type.SELECT_FAV)
        .bundle(favBundle)
        .build());
    actions.put(Const.ACTION_MAIN, new Action.Builder()
        .type(Action.Type.JUMP)
        .bundle(bundle)
        .transitionName(EarthApplication.getInstance().getString(R.string.art_detail_transition_name))
        .uri(NavigationManager.buildUri(Uri.parse(art.url).getEncodedPath()))
        .build());

    return builder.actions(actions).build();
  }

  public static Model createModelFromTag(Tag tag, Model.Template template) {
    tag = DataVerifier.verify(tag);
    if (tag == null) {
      return null;
    }
    List<Model> subModels = new ArrayList<>();
    for (Tag subTag: tag.values) {
      subModels.add(createModelFromTag(subTag, Model.Template.CHILD_TAG));
    }
    Flag flag = new Flag.Builder().is_selected(false).build();
    Map<Integer, Action> actions = new HashMap<>();
    actions.put(Const.ACTION_MAIN, new Action.Builder()
        .type(Action.Type.EVENT)
        .clazz(OnClickTagItemEvent.class)
        .build());
    return new Model.Builder()
        .type(Model.Type.TAG)
        .template(template)
        .tag(tag)
        .identity(tag.id)
        .title(tag.key)
        .url(tag.url)
        .subModels(subModels)
        .flag(flag)
        .actions(actions)
        .build();
  }

  public static Model createModelFromImage(Image image, Model.Template template) {
    image = DataVerifier.verify(image);
    if (image == null) {
      return null;
    }
    Count count = new Count.Builder()
        .number(image.number)
        .width(image.width)
        .height(image.height)
        .x_offset(image.offset)
        .pages(image.pages)
        .build();
    Flag flag = new Flag.Builder().is_thumbnail(image.is_thumbnail).build();
    Map<Integer, Action> actions = new HashMap<>();
    Bundle bundle = new Bundle();
    bundle.putInt(Const.EXTRA_PAGES, count.pages);
    bundle.putString(Const.EXTRA_TOKEN, image.gallery_token);
    actions.put(Const.ACTION_MAIN, new Action.Builder()
        .type(Action.Type.JUMP_IMAGE)
        .uri(NavigationManager.buildUri(Uri.parse(image.origin_url).getEncodedPath()))
        .bundle(bundle)
        .build());
    return new Model.Builder()
        .type(Model.Type.IMAGE)
        .template(template)
        .image(image)
        .token(image.gallery_token)
        .count(count)
        .url(image.origin_url)
        .flag(flag)
        .cover(image.src)
        .actions(actions)
        .build();
  }

  public static Model createModelFromFav(Fav fav, Model.Template template) {
    fav = DataVerifier.verify(fav);
    if (fav == null) {
      return null;
    }
    return new Model.Builder()
        .type(Model.Type.FAV)
        .template(template)
        .fav(fav)
        .identity(fav.id)
        .title(fav.name)
        .description(fav.apply)
        .build();
  }

  public static Model createModelFromCategory(Model.Category category, Model.Template template) {
    if (category == null) {
      return null;
    }
    Flag flag = new Flag.Builder()
        .is_selected(EarthApplication.getInstance().getSelectedCategory().contains(category))
        .build();
    Map<Integer, Action> actions = new HashMap<>();
    actions.put(Const.ACTION_MAIN, new Action.Builder()
        .type(Action.Type.SELECT_CATEGORY)
        .build());
    return new Model.Builder()
        .category(category)
        .template(template)
        .title(category.getName())
        .flag(flag)
        .actions(actions)
        .build();
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

  public static Model createModelFromComment(Comment comment, Model.Template template) {
    comment = DataVerifier.verify(comment);
    if (comment == null) {
      return null;
    }
    Count count = new Count.Builder().score(comment.score).build();
    return new Model.Builder()
        .type(Model.Type.COMMENT)
        .template(template)
        .comment(comment)
        .title(comment.author)
        .date(comment.date)
        .description(comment.content)
        .count(count)
        .build();
  }

  public static Model createModelFromPicture(Picture picture, Model.Template template) {
    picture = DataVerifier.verify(picture);
    if (picture == null) {
      return null;
    }
    Model model = createModelFromImage(picture.image, template);
    if (model != null) {
      Map<Integer, Action> actions = new HashMap<>();
      return model.newBuilder()
          .type(Model.Type.PICTURE)
          .picture(picture)
          .image(picture.image)
          .title(picture.name)
          .actions(actions)
          .build();
    }
    return null;
  }
}