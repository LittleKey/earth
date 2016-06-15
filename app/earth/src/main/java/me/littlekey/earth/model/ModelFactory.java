package me.littlekey.earth.model;

import android.os.Bundle;
import android.text.TextUtils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Category;
import me.littlekey.earth.model.proto.Count;
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
        .is_me(EarthApplication.getInstance().getAccountManager().isSelf(user.display_name))
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

  public static Model createModelFromArtElement(Element element, Model.Template template) {
    element = DataVerifier.verify(element);
    if (element == null) {
      return null;
    }
    Elements children = element.children();
    // NOTE : find category
    Element categoryEle = children.get(0);
    Category category = new Category.Builder()
        .img(categoryEle.select("a > img").attr("src"))
        .name(categoryEle.select("a > img").attr("alt"))
        .build();
    // NOTE : find date
    Element dateEle = children.get(1);
    String date = dateEle.text();
    // NOTE : find cover and title and url and rank
    Element artEle = children.get(2);
    String cover =  artEle.select("div.it2 > img").attr("src");
    if (TextUtils.isEmpty(cover)) {
      Pattern coverPattern = Pattern.compile("init~(.*?)~(.*?)/(.*?)~");
      Matcher coverMatcher = coverPattern.matcher(artEle.select("div.it2").text());
      if (coverMatcher.find()) {
        cover = String.format("http://%s/%s/%s",
            coverMatcher.group(1), coverMatcher.group(2), coverMatcher.group(3));
      }
    }
    Elements titleAndUrlElements = artEle.select("div.it5 > a");
    String arUrl  = titleAndUrlElements.attr("href");
    String title = titleAndUrlElements.text();
    Pattern rankPattern = Pattern.compile("background-position:\\s*?-?(\\d*?)px\\s*?-?(\\d*?)px;");
    Matcher rankMatcher = rankPattern.matcher(artEle.select("div.it4 > div").attr("style"));
    int rankNum = 0;
    float rankOffset = 0f;
    if (rankMatcher.find()) {
      rankNum = 5 - Integer.valueOf(rankMatcher.group(1)) / 16;
      rankOffset = Integer.valueOf(rankMatcher.group(2)) == 1 ? 0.0f : 0.5f;
    }
    Count count = new Count.Builder().rank(rankNum - rankOffset).build();
    // NOTE : find publisher
    Element publisherEle = children.get(3);
    Elements nameAndUrlElements = publisherEle.select("div > a");
//    String publisherUrl = nameAndUrlElements.attr("href");
    String publisherName = nameAndUrlElements.text();

    Model.Builder builder = new Model.Builder()
        .type(Model.Type.ART)
        .template(template)
        .title(title)
        .url(arUrl)
        .subtitle(publisherName)
        .category(category)
        .date(date)
        .count(count)
        .cover(cover);
    return builder.build();
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