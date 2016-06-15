package me.littlekey.earth.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.wire.WireEnum;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.model.proto.User;

/**
 * Created by littlekey on 16/6/10.
 */

public final class Model implements Parcelable {

  public static final Long DEFAULT_IDENTITY = 0L;
  public static final Type DEFAULT_TYPE = Type.UNKNOWN;
  public static final Template DEFAULT_TEMPLATE = Template.UNSUPPORTED;
  public static final String DEFAULT_TITLE = "";
  public static final String DEFAULT_SUBTITLE = "";
  public static final String DEFAULT_DESCRIPTION = "";
  public static final String DEFAULT_URL = "";
  public static final String DEFAULT_COVER = "";
  public static final Integer DEFAULT_INDEX = 0;
  public static final Map<Integer, Action> DEFAULT_ACTIONS = new HashMap<>();
  public static final Creator<Model> CREATOR = new Creator<Model>() {
    @Override
    public Model createFromParcel(Parcel source) {
      // TODO add other type.
      try {
        Type type = Type.values()[source.readInt()];
        Template template = Template.values()[source.readInt()];
        byte[] bytes = source.createByteArray();
        switch (type) {
          case USER:
            User user = User.ADAPTER.decode(bytes);
            return ModelFactory.createModelFromUser(user, template);
          default:
            return null;
        }
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }

    @Override
    public Model[] newArray(int size) {
      return new Model[size];
    }
  };
  private final String identity;
  private final Type type;
  private final Template template;
  private final String title;
  private final String subtitle;
  private final String userName;
  private final String description;
  private final String url;
  private final String cover;
  private final String avatar;
  private final String date;
  private final User user;
  private final Count count;
  private final Member member;
  private final Flag flag;
  private final Category category;
  private List<Model> subModels;
  private Map<Integer, Action> actions;
  private int hashCode;

  private Model(Builder builder) {
    this.identity = builder.identity;
    this.type = builder.type;
    this.template = builder.template;
    this.title = builder.title;
    this.subtitle = builder.subtitle;
    this.userName = builder.userName;
    this.description = builder.description;
    this.url = builder.url;
    this.cover = builder.cover;
    this.avatar = builder.avatar;
    this.date = builder.date;
    this.user = builder.user;
    this.count = builder.count;
    this.member = builder.member;
    this.flag = builder.flag;
    this.category = builder.category;
    this.subModels = immutableCopyOf(builder.subModels);
    if (builder.actions == null) {
      this.actions = new HashMap<>();
    } else {
      this.actions = new HashMap<>(builder.actions);
    }
  }

  protected static <T> List<T> immutableCopyOf(List<T> source) {
    if (source == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(new ArrayList<>(source));
  }

  public String getIdentity() {
    return identity;
  }

  public Type getType() {
    return type;
  }

  public Template getTemplate() {
    return template;
  }

  public String getTitle() {
    return title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  @Deprecated
  public String getUserName() {
    return userName;
  }

  public String getDescription() {
    return description;
  }

  public String getUrl() {
    return url;
  }

  public String getCover() {
    return cover;
  }

  @Deprecated
  public String getAvatar() {
    return avatar;
  }

  public String getDate() {
    return date;
  }

  public User getUser() {
    return user;
  }

  public Count getCount() {
    return count;
  }

  public Member getMember() {
    return member;
  }

  public Flag getFlag() {
    return flag;
  }

  public Category getCategory() {
    return category;
  }

  public List<Model> getSubModels() {
    return subModels;
  }

  public Map<Integer, Action> getActions() {
    return actions;
  }

  /**
   * Notice: We need using this method when data deduplication, so we override this method to make
   * sure that only field that store original data should count.
   *
   * @param other other Model
   * @return {@code true} if the specified object is equal to this {@code Model}; {@code false}
   *         otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Model)) return false;
    Model o = (Model) other;
    return equals(identity, o.identity)
        && equals(type, o.type)
        && equals(template, o.template)
        && equals(title, o.title)
        && equals(subtitle, o.subtitle)
        && equals(userName, o.userName)
        && equals(description, o.description)
        && equals(url, o.url)
        && equals(cover, o.cover)
        && equals(avatar, o.avatar)
        && equals(date, o.date)
        && equals(user, o.user)
        && equals(count, o.count)
        && equals(member, o.member)
        && equals(flag, o.flag)
        && equals(category, o.category)
        && equals(subModels, o.subModels);
  }

  /**
   * Notice: We need using this method when data deduplication, so we override this method to make
   * sure that only field that store original data should count.
   *
   * @return this model's hash code.
   */
  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = identity != null ? identity.hashCode() : 0;
      result = result * 37 + (type != null ? type.hashCode() : 0);
      result = result * 37 + (template != null ? template.hashCode() : 0);
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (subtitle != null ? subtitle.hashCode() : 0);
      result = result * 37 + (userName != null ? userName.hashCode() : 0);
      result = result * 37 + (description != null ? description.hashCode() : 0);
      result = result * 37 + (url != null ? url.hashCode() : 0);
      result = result * 37 + (cover != null ? cover.hashCode() : 0);
      result = result * 37 + (avatar != null ? avatar.hashCode() : 0);
      result = result * 37 + (date != null ? date.hashCode() : 0);
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (count != null ? count.hashCode() : 0);
      result = result * 37 + (member != null ? member.hashCode() : 0);
      result = result * 37 + (flag != null ? flag.hashCode() : 0);
      result = result * 37 + (category != null ? category.hashCode() : 0);
      result = result * 37 + (subModels != null ? subModels.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  protected boolean equals(Object a, Object b) {
    return a == b || (a != null && a.equals(b));
  }

  protected boolean equals(List<?> a, List<?> b) {
    // Canonicalize empty -> null
    if (a != null && a.isEmpty()) a = null;
    if (b != null && b.isEmpty()) b = null;
    return a == b || (a != null && a.equals(b));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    // TODO add other type.
    dest.writeInt(type.getValue());
    dest.writeInt(template.getValue());
    switch (type) {
      case USER:
        dest.writeByteArray(user.encode());
        break;
    }
  }

  public enum Type implements WireEnum {
    UNKNOWN(0),
    LINK(1),
    USER(2),
    ART(3);

    private final int value;

    Type(int value) {
      this.value = value;
    }

    @Override
    public int getValue() {
      return value;
    }
  }

  public enum Template implements WireEnum {
    UNSUPPORTED(0),
    DATA(1),
    HEADER(2),
    USER(3),
    ITEM_ART(4);

    private final int value;

    Template(int value) {
      this.value = value;
    }

    @Override
    public int getValue() {
      return value;
    }
  }

  public enum Category implements WireEnum {
    UNKNOWN(0),
    DOUJINSHI(1),
    MANGA(2),
    ARTIST_CG(3),
    GAME_CG(4),
    WESTERN(5),
    NON__H(6),
    IMAGE_SET(7),
    COSPLAY(8),
    ASIAN_PORN(9),
    MISC(10);

    private final int value;

    Category(int value) {
      this.value = value;
    }

    @Override
    public int getValue() {
      return value;
    }

    public String getName() {
      return name().replace("__", "-").replace("_", " ").toUpperCase();
    }

    public static Category from(String name) {
      switch (name) {
        case "imageset":
          return IMAGE_SET;
        case "artistcg":
          return ARTIST_CG;
        case "gamecg":
          return GAME_CG;
        case "asianporn":
          return ASIAN_PORN;
        default:
          return valueOf(Category.class, name.replace(" ", "_").replace("-", "__").toUpperCase());
      }
    }
  }

  public static final class Builder {

    public String identity;
    public Type type;
    public Template template;
    public String title;
    public String subtitle;
    public String userName;
    public String description;
    public String url;
    public String cover;
    public String avatar;
    public String date;
    public User user;
    public Count count;
    public Member member;
    public Flag flag;
    public Category category;
    public List<Model> subModels;
    public Map<Integer, Action> actions;

    public Builder() {}

    public Builder(Model message) {
      if (message == null) return;
      this.identity = message.identity;
      this.type = message.type;
      this.template = message.template;
      this.title = message.title;
      this.subtitle = message.subtitle;
      this.userName = message.userName;
      this.description = message.description;
      this.url = message.url;
      this.cover = message.cover;
      this.avatar = message.avatar;
      this.date = message.date;
      this.user = message.user;
      this.count = message.count;
      this.member = message.member;
      this.flag = message.flag;
      this.category = message.category;
      this.subModels = copyOf(message.subModels);
      this.actions = copyOf(message.actions);
    }

    protected static <T> List<T> copyOf(List<T> source) {
      return source == null ? null : new ArrayList<>(source);
    }

    protected static <T> List<T> checkForNulls(List<T> elements) {
      if (elements != null && !elements.isEmpty()) {
        for (T element : elements) {
          if (element == null) {
            throw new NullPointerException();
          }
        }
      }
      return elements;
    }

    protected static <K, V> Map<K, V> copyOf(Map<K, V> source) {
      return source == null ? null : new HashMap<>(source);
    }

    public Builder identity(String identity) {
      this.identity = identity;
      return this;
    }

    public Builder type(Type type) {
      this.type = type;
      return this;
    }

    public Builder template(Template template) {
      this.template = template;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder subtitle(String subtitle) {
      this.subtitle = subtitle;
      return this;
    }

    @Deprecated
    public Builder userName(String name) {
      this.userName = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder cover(String cover) {
      this.cover = cover;
      return this;
    }

    @Deprecated
    public Builder avatar(String avatar) {
      this.avatar = avatar;
      return this;
    }

    public Builder date(String date) {
      this.date = date;
      return this;
    }

    public Builder user(User user) {
      this.user = user;
      return this;
    }

    public Builder count(Count count) {
      this.count = count;
      return this;
    }

    public Builder member(Member member) {
      this.member = member;
      return this;
    }

    public Builder flag(Flag flag) {
      this.flag = flag;
      return this;
    }

    public Builder category(Category category) {
      this.category = category;
      return this;
    }

    public Builder subModels(List<Model> subModels) {
      this.subModels = checkForNulls(subModels);
      return this;
    }

    public Builder actions(Map<Integer, Action> actions) {
      this.actions = actions;
      return this;
    }

    public Model build() {
      return new Model(this);
    }
  }
}
