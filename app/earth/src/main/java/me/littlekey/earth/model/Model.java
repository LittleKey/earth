// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: model.proto at 9:1
package me.littlekey.earth.model;

import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireEnum;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.R;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.Comment;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Fav;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.model.proto.Image;
import me.littlekey.earth.model.proto.Picture;
import me.littlekey.earth.model.proto.Tag;
import me.littlekey.earth.model.proto.User;
import me.littlekey.earth.utils.EarthUtils;
import okio.ByteString;

public final class Model extends Message<Model, Model.Builder> implements Parcelable {
  public static final ProtoAdapter<Model> ADAPTER = new ProtoAdapter_Model();

  public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {
    @Override
    public Model createFromParcel(Parcel source) {
      // TODO add other type.
      try {
        Type type = Type.values()[source.readInt()];
        Template template = Template.values()[source.readInt()];
        byte[] bytes = source.createByteArray();
        switch (type) {
          case ART:
            Art art = Art.ADAPTER.decode(bytes);
            return ModelFactory.createModelFromArt(art, template);
          case TAG:
            Tag tag = Tag.ADAPTER.decode(bytes);
            return ModelFactory.createModelFromTag(tag, template);
          case IMAGE:
            Image image = Image.ADAPTER.decode(bytes);
            return ModelFactory.createModelFromImage(image, template);
          case FAV:
            Fav fav = Fav.ADAPTER.decode(bytes);
            return ModelFactory.createModelFromFav(fav, template);
          case COMMENT:
            Comment comment = Comment.ADAPTER.decode(bytes);
            return ModelFactory.createModelFromComment(comment, template);
          case PICTURE:
            Picture picture = Picture.ADAPTER.decode(bytes);
            return ModelFactory.createModelFromPicture(picture, template);
          default:
            throw new ParcelFormatException(String.format("can not parcel '%s'", type.name()));
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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(type.getValue());
    dest.writeInt(template.getValue());
    switch (type) {
      case ART:
        dest.writeByteArray(art.encode());
        break;
      case TAG:
        dest.writeByteArray(tag.encode());
        break;
      case IMAGE:
        dest.writeByteArray(image.encode());
        break;
      case FAV:
        dest.writeByteArray(fav.encode());
        break;
      case COMMENT:
        dest.writeByteArray(comment.encode());
        break;
      case PICTURE:
        dest.writeByteArray(picture.encode());
        break;
      default:
        break;
    }
  }

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_IDENTITY = "";

  public static final String DEFAULT_TOKEN = "";

  public static final Type DEFAULT_TYPE = Type.NONE;

  public static final Template DEFAULT_TEMPLATE = Template.UNSUPPORTED;

  public static final String DEFAULT_TITLE = "";

  public static final String DEFAULT_SUBTITLE = "";

  public static final String DEFAULT_LANGUAGE = "";

  public static final String DEFAULT_FILESIZE = "";

  public static final String DEFAULT_DESCRIPTION = "";

  public static final String DEFAULT_URL = "";

  public static final String DEFAULT_COVER = "";

  public static final String DEFAULT_DATE = "";

  public static final Category DEFAULT_CATEGORY = Category.UNKNOWN;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String identity;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String token;

  @WireField(
      tag = 3,
      adapter = "me.littlekey.earth.model.proto.Model$Type#ADAPTER"
  )
  public final Type type;

  @WireField(
      tag = 4,
      adapter = "me.littlekey.earth.model.proto.Model$Template#ADAPTER"
  )
  public final Template template;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String title;

  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String subtitle;

  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String language;

  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String fileSize;

  @WireField(
      tag = 9,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String description;

  @WireField(
      tag = 10,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String url;

  @WireField(
      tag = 11,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String cover;

  @WireField(
      tag = 12,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String date;

  @WireField(
      tag = 13,
      adapter = "me.littlekey.earth.model.proto.User#ADAPTER"
  )
  public final User user;

  @WireField(
      tag = 14,
      adapter = "me.littlekey.earth.model.proto.Art#ADAPTER"
  )
  public final Art art;

  @WireField(
      tag = 15,
      adapter = "me.littlekey.earth.model.proto.Tag#ADAPTER"
  )
  public final Tag tag;

  @WireField(
      tag = 16,
      adapter = "me.littlekey.earth.model.proto.Image#ADAPTER"
  )
  public final Image image;

  @WireField(
      tag = 17,
      adapter = "me.littlekey.earth.model.proto.Fav#ADAPTER"
  )
  public final Fav fav;

  @WireField(
      tag = 18,
      adapter = "me.littlekey.earth.model.proto.Comment#ADAPTER"
  )
  public final Comment comment;

  @WireField(
      tag = 19,
      adapter = "me.littlekey.earth.model.proto.Count#ADAPTER"
  )
  public final Count count;

  @WireField(
      tag = 20,
      adapter = "me.littlekey.earth.model.proto.Flag#ADAPTER"
  )
  public final Flag flag;

  @WireField(
      tag = 21,
      adapter = "me.littlekey.earth.model.proto.Model$Category#ADAPTER"
  )
  public final Category category;

  @WireField(
      tag = 22,
      adapter = "me.littlekey.earth.model.proto.Model#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<Model> subModels;

  @WireField(
      tag = 23,
      adapter = "me.littlekey.earth.model.proto.Picture#ADAPTER"
  )
  public final Picture picture;

  public final Map<Integer, Action> actions;

  public Model(String identity, String token, Type type, Template template, String title, String subtitle, String language, String fileSize, String description, String url, String cover, String date, User user, Art art, Tag tag, Image image, Fav fav, Comment comment, Count count, Flag flag, Category category, List<Model> subModels, Picture picture, Map<Integer, Action> actions) {
    this(identity, token, type, template, title, subtitle, language, fileSize, description, url, cover, date, user, art, tag, image, fav, comment, count, flag, category, subModels, picture, actions, ByteString.EMPTY);
  }

  public Model(String identity, String token, Type type, Template template, String title, String subtitle, String language, String fileSize, String description, String url, String cover, String date, User user, Art art, Tag tag, Image image, Fav fav, Comment comment, Count count, Flag flag, Category category, List<Model> subModels, Picture picture, Map<Integer, Action> actions, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.identity = identity;
    this.token = token;
    this.type = type;
    this.template = template;
    this.title = title;
    this.subtitle = subtitle;
    this.language = language;
    this.fileSize = fileSize;
    this.description = description;
    this.url = url;
    this.cover = cover;
    this.date = date;
    this.user = user;
    this.art = art;
    this.tag = tag;
    this.image = image;
    this.fav = fav;
    this.comment = comment;
    this.count = count;
    this.flag = flag;
    this.category = category;
    this.subModels = Internal.immutableCopyOf("subModels", subModels);
    this.picture = picture;
    this.actions = Internal.immutableCopyOf("actions", actions);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.identity = identity;
    builder.token = token;
    builder.type = type;
    builder.template = template;
    builder.title = title;
    builder.subtitle = subtitle;
    builder.language = language;
    builder.fileSize = fileSize;
    builder.description = description;
    builder.url = url;
    builder.cover = cover;
    builder.date = date;
    builder.user = user;
    builder.art = art;
    builder.tag = tag;
    builder.image = image;
    builder.fav = fav;
    builder.comment = comment;
    builder.count = count;
    builder.flag = flag;
    builder.category = category;
    builder.subModels = Internal.copyOf("subModels", subModels);
    builder.picture = picture;
    builder.actions = Internal.copyOf("actions", actions);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Model)) return false;
    Model o = (Model) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(identity, o.identity)
        && Internal.equals(token, o.token)
        && Internal.equals(type, o.type)
        && Internal.equals(template, o.template)
        && Internal.equals(title, o.title)
        && Internal.equals(subtitle, o.subtitle)
        && Internal.equals(language, o.language)
        && Internal.equals(fileSize, o.fileSize)
        && Internal.equals(description, o.description)
        && Internal.equals(url, o.url)
        && Internal.equals(cover, o.cover)
        && Internal.equals(date, o.date)
        && Internal.equals(user, o.user)
        && Internal.equals(art, o.art)
        && Internal.equals(tag, o.tag)
        && Internal.equals(image, o.image)
        && Internal.equals(fav, o.fav)
        && Internal.equals(comment, o.comment)
        && Internal.equals(count, o.count)
        && Internal.equals(flag, o.flag)
        && Internal.equals(category, o.category)
        && subModels.equals(o.subModels)
        && Internal.equals(picture, o.picture);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (identity != null ? identity.hashCode() : 0);
      result = result * 37 + (token != null ? token.hashCode() : 0);
      result = result * 37 + (type != null ? type.hashCode() : 0);
      result = result * 37 + (template != null ? template.hashCode() : 0);
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (subtitle != null ? subtitle.hashCode() : 0);
      result = result * 37 + (language != null ? language.hashCode() : 0);
      result = result * 37 + (fileSize != null ? fileSize.hashCode() : 0);
      result = result * 37 + (description != null ? description.hashCode() : 0);
      result = result * 37 + (url != null ? url.hashCode() : 0);
      result = result * 37 + (cover != null ? cover.hashCode() : 0);
      result = result * 37 + (date != null ? date.hashCode() : 0);
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (art != null ? art.hashCode() : 0);
      result = result * 37 + (tag != null ? tag.hashCode() : 0);
      result = result * 37 + (image != null ? image.hashCode() : 0);
      result = result * 37 + (fav != null ? fav.hashCode() : 0);
      result = result * 37 + (comment != null ? comment.hashCode() : 0);
      result = result * 37 + (count != null ? count.hashCode() : 0);
      result = result * 37 + (flag != null ? flag.hashCode() : 0);
      result = result * 37 + (category != null ? category.hashCode() : 0);
      result = result * 37 + subModels.hashCode();
      result = result * 37 + (picture != null ? picture.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (identity != null) builder.append(", identity=").append(identity);
    if (token != null) builder.append(", token=").append(token);
    if (type != null) builder.append(", type=").append(type);
    if (template != null) builder.append(", template=").append(template);
    if (title != null) builder.append(", title=").append(title);
    if (subtitle != null) builder.append(", subtitle=").append(subtitle);
    if (language != null) builder.append(", language=").append(language);
    if (fileSize != null) builder.append(", fileSize=").append(fileSize);
    if (description != null) builder.append(", description=").append(description);
    if (url != null) builder.append(", url=").append(url);
    if (cover != null) builder.append(", cover=").append(cover);
    if (date != null) builder.append(", date=").append(date);
    if (user != null) builder.append(", user=").append(user);
    if (art != null) builder.append(", art=").append(art);
    if (tag != null) builder.append(", tag=").append(tag);
    if (image != null) builder.append(", image=").append(image);
    if (fav != null) builder.append(", fav=").append(fav);
    if (comment != null) builder.append(", comment=").append(comment);
    if (count != null) builder.append(", count=").append(count);
    if (flag != null) builder.append(", flag=").append(flag);
    if (category != null) builder.append(", category=").append(category);
    if (!subModels.isEmpty()) builder.append(", subModels=").append(subModels);
    if (picture != null) builder.append(", picture=").append(picture);
    if (!actions.isEmpty()) builder.append(", actions=").append(actions);
    return builder.replace(0, 2, "Model{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Model, Builder> {
    public String identity;

    public String token;

    public Type type;

    public Template template;

    public String title;

    public String subtitle;

    public String language;

    public String fileSize;

    public String description;

    public String url;

    public String cover;

    public String date;

    public User user;

    public Art art;

    public Tag tag;

    public Image image;

    public Fav fav;

    public Comment comment;

    public Count count;

    public Flag flag;

    public Category category;

    public List<Model> subModels;

    public Picture picture;

    public Map<Integer, Action> actions;

    public Builder() {
      subModels = Internal.newMutableList();
      actions = Internal.newMutableMap();
    }

    public Builder identity(String identity) {
      this.identity = identity;
      return this;
    }

    public Builder token(String token) {
      this.token = token;
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

    public Builder language(String language) {
      this.language = language;
      return this;
    }

    public Builder fileSize(String fileSize) {
      this.fileSize = fileSize;
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

    public Builder date(String date) {
      this.date = date;
      return this;
    }

    public Builder user(User user) {
      this.user = user;
      return this;
    }

    public Builder art(Art art) {
      this.art = art;
      return this;
    }

    public Builder tag(Tag tag) {
      this.tag = tag;
      return this;
    }

    public Builder image(Image image) {
      this.image = image;
      return this;
    }

    public Builder fav(Fav fav) {
      this.fav = fav;
      return this;
    }

    public Builder comment(Comment comment) {
      this.comment = comment;
      return this;
    }

    public Builder count(Count count) {
      this.count = count;
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
      Internal.checkElementsNotNull(subModels);
      this.subModels = subModels;
      return this;
    }

    public Builder picture(Picture picture) {
      this.picture = picture;
      return this;
    }

    public Builder actions(Map<Integer, Action> actions) {
      Internal.checkElementsNotNull(actions);
      this.actions = actions;
      return this;
    }

    @Override
    public Model build() {
      return new Model(identity, token, type, template, title, subtitle, language, fileSize, description, url, cover, date, user, art, tag, image, fav, comment, count, flag, category, subModels, picture, actions, super.buildUnknownFields());
    }
  }

  public enum Type implements WireEnum {
    NONE(0),

    LINK(1),

    ART(2),

    TAG(3),

    IMAGE(4),

    FAV(5),

    TEXT(6),

    COMMENT(7),

    PICTURE(8);

    public static final ProtoAdapter<Type> ADAPTER = ProtoAdapter.newEnumAdapter(Type.class);

    private final int value;

    Type(int value) {
      this.value = value;
    }

    /**
     * Return the constant for {@code value} or null.
     */
    public static Type fromValue(int value) {
      switch (value) {
        case 0: return NONE;
        case 1: return LINK;
        case 2: return ART;
        case 3: return TAG;
        case 4: return IMAGE;
        case 5: return FAV;
        case 6: return TEXT;
        case 7: return COMMENT;
        case 8: return PICTURE;
        default: return null;
      }
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

    ITEM_ART(4),

    PARENT_TAG(5),

    CHILD_TAG(6),

    PREVIEW_IMAGE(7),

    SELECT_FAV(8),

    CATEGORY(9),

    TITLE(10),

    ITEM_COMMENT(11),

    PAGE_PICTURE(12);

    public static final ProtoAdapter<Template> ADAPTER = ProtoAdapter.newEnumAdapter(Template.class);

    private final int value;

    Template(int value) {
      this.value = value;
    }

    /**
     * Return the constant for {@code value} or null.
     */
    public static Template fromValue(int value) {
      switch (value) {
        case 0: return UNSUPPORTED;
        case 1: return DATA;
        case 2: return HEADER;
        case 3: return USER;
        case 4: return ITEM_ART;
        case 5: return PARENT_TAG;
        case 6: return CHILD_TAG;
        case 7: return PREVIEW_IMAGE;
        case 8: return SELECT_FAV;
        case 9: return CATEGORY;
        case 10: return TITLE;
        case 11: return ITEM_COMMENT;
        case 12: return PAGE_PICTURE;
        default: return null;
      }
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

    public static final ProtoAdapter<Category> ADAPTER = ProtoAdapter.newEnumAdapter(Category.class);

    private final int value;

    Category(int value) {
      this.value = value;
    }

    /**
     * Return the constant for {@code value} or null.
     */
    public static Category fromValue(int value) {
      switch (value) {
        case 0: return UNKNOWN;
        case 1: return DOUJINSHI;
        case 2: return MANGA;
        case 3: return ARTIST_CG;
        case 4: return GAME_CG;
        case 5: return WESTERN;
        case 6: return NON__H;
        case 7: return IMAGE_SET;
        case 8: return COSPLAY;
        case 9: return ASIAN_PORN;
        case 10: return MISC;
        default: return null;
      }
    }

    @Override
    public int getValue() {
      return value;
    }

    public String getName() {
      return name().replace("__", "-").replace("_", " ").toUpperCase();
    }

    public String getSearchName() {
      return EarthUtils.formatString(R.string.category_search_key,
          name().replace("__", "-").replace("_", "").toLowerCase());
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

  private static final class ProtoAdapter_Model extends ProtoAdapter<Model> {
    ProtoAdapter_Model() {
      super(FieldEncoding.LENGTH_DELIMITED, Model.class);
    }

    @Override
    public int encodedSize(Model value) {
      return (value.identity != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.identity) : 0)
          + (value.token != null ? ProtoAdapter.STRING.encodedSizeWithTag(2, value.token) : 0)
          + (value.type != null ? Type.ADAPTER.encodedSizeWithTag(3, value.type) : 0)
          + (value.template != null ? Template.ADAPTER.encodedSizeWithTag(4, value.template) : 0)
          + (value.title != null ? ProtoAdapter.STRING.encodedSizeWithTag(5, value.title) : 0)
          + (value.subtitle != null ? ProtoAdapter.STRING.encodedSizeWithTag(6, value.subtitle) : 0)
          + (value.language != null ? ProtoAdapter.STRING.encodedSizeWithTag(7, value.language) : 0)
          + (value.fileSize != null ? ProtoAdapter.STRING.encodedSizeWithTag(8, value.fileSize) : 0)
          + (value.description != null ? ProtoAdapter.STRING.encodedSizeWithTag(9, value.description) : 0)
          + (value.url != null ? ProtoAdapter.STRING.encodedSizeWithTag(10, value.url) : 0)
          + (value.cover != null ? ProtoAdapter.STRING.encodedSizeWithTag(11, value.cover) : 0)
          + (value.date != null ? ProtoAdapter.STRING.encodedSizeWithTag(12, value.date) : 0)
          + (value.user != null ? User.ADAPTER.encodedSizeWithTag(13, value.user) : 0)
          + (value.art != null ? Art.ADAPTER.encodedSizeWithTag(14, value.art) : 0)
          + (value.tag != null ? Tag.ADAPTER.encodedSizeWithTag(15, value.tag) : 0)
          + (value.image != null ? Image.ADAPTER.encodedSizeWithTag(16, value.image) : 0)
          + (value.fav != null ? Fav.ADAPTER.encodedSizeWithTag(17, value.fav) : 0)
          + (value.comment != null ? Comment.ADAPTER.encodedSizeWithTag(18, value.comment) : 0)
          + (value.count != null ? Count.ADAPTER.encodedSizeWithTag(19, value.count) : 0)
          + (value.flag != null ? Flag.ADAPTER.encodedSizeWithTag(20, value.flag) : 0)
          + (value.category != null ? Category.ADAPTER.encodedSizeWithTag(21, value.category) : 0)
          + Model.ADAPTER.asRepeated().encodedSizeWithTag(22, value.subModels)
          + (value.picture != null ? Picture.ADAPTER.encodedSizeWithTag(23, value.picture) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Model value) throws IOException {
      if (value.identity != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.identity);
      if (value.token != null) ProtoAdapter.STRING.encodeWithTag(writer, 2, value.token);
      if (value.type != null) Type.ADAPTER.encodeWithTag(writer, 3, value.type);
      if (value.template != null) Template.ADAPTER.encodeWithTag(writer, 4, value.template);
      if (value.title != null) ProtoAdapter.STRING.encodeWithTag(writer, 5, value.title);
      if (value.subtitle != null) ProtoAdapter.STRING.encodeWithTag(writer, 6, value.subtitle);
      if (value.language != null) ProtoAdapter.STRING.encodeWithTag(writer, 7, value.language);
      if (value.fileSize != null) ProtoAdapter.STRING.encodeWithTag(writer, 8, value.fileSize);
      if (value.description != null) ProtoAdapter.STRING.encodeWithTag(writer, 9, value.description);
      if (value.url != null) ProtoAdapter.STRING.encodeWithTag(writer, 10, value.url);
      if (value.cover != null) ProtoAdapter.STRING.encodeWithTag(writer, 11, value.cover);
      if (value.date != null) ProtoAdapter.STRING.encodeWithTag(writer, 12, value.date);
      if (value.user != null) User.ADAPTER.encodeWithTag(writer, 13, value.user);
      if (value.art != null) Art.ADAPTER.encodeWithTag(writer, 14, value.art);
      if (value.tag != null) Tag.ADAPTER.encodeWithTag(writer, 15, value.tag);
      if (value.image != null) Image.ADAPTER.encodeWithTag(writer, 16, value.image);
      if (value.fav != null) Fav.ADAPTER.encodeWithTag(writer, 17, value.fav);
      if (value.comment != null) Comment.ADAPTER.encodeWithTag(writer, 18, value.comment);
      if (value.count != null) Count.ADAPTER.encodeWithTag(writer, 19, value.count);
      if (value.flag != null) Flag.ADAPTER.encodeWithTag(writer, 20, value.flag);
      if (value.category != null) Category.ADAPTER.encodeWithTag(writer, 21, value.category);
      Model.ADAPTER.asRepeated().encodeWithTag(writer, 22, value.subModels);
      if (value.picture != null) Picture.ADAPTER.encodeWithTag(writer, 23, value.picture);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Model decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.identity(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.token(ProtoAdapter.STRING.decode(reader)); break;
          case 3: {
            try {
              builder.type(Type.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 4: {
            try {
              builder.template(Template.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 5: builder.title(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.subtitle(ProtoAdapter.STRING.decode(reader)); break;
          case 7: builder.language(ProtoAdapter.STRING.decode(reader)); break;
          case 8: builder.fileSize(ProtoAdapter.STRING.decode(reader)); break;
          case 9: builder.description(ProtoAdapter.STRING.decode(reader)); break;
          case 10: builder.url(ProtoAdapter.STRING.decode(reader)); break;
          case 11: builder.cover(ProtoAdapter.STRING.decode(reader)); break;
          case 12: builder.date(ProtoAdapter.STRING.decode(reader)); break;
          case 13: builder.user(User.ADAPTER.decode(reader)); break;
          case 14: builder.art(Art.ADAPTER.decode(reader)); break;
          case 15: builder.tag(Tag.ADAPTER.decode(reader)); break;
          case 16: builder.image(Image.ADAPTER.decode(reader)); break;
          case 17: builder.fav(Fav.ADAPTER.decode(reader)); break;
          case 18: builder.comment(Comment.ADAPTER.decode(reader)); break;
          case 19: builder.count(Count.ADAPTER.decode(reader)); break;
          case 20: builder.flag(Flag.ADAPTER.decode(reader)); break;
          case 21: {
            try {
              builder.category(Category.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 22: builder.subModels.add(Model.ADAPTER.decode(reader)); break;
          case 23: builder.picture(Picture.ADAPTER.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public Model redact(Model value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = User.ADAPTER.redact(builder.user);
      if (builder.art != null) builder.art = Art.ADAPTER.redact(builder.art);
      if (builder.tag != null) builder.tag = Tag.ADAPTER.redact(builder.tag);
      if (builder.image != null) builder.image = Image.ADAPTER.redact(builder.image);
      if (builder.fav != null) builder.fav = Fav.ADAPTER.redact(builder.fav);
      if (builder.comment != null) builder.comment = Comment.ADAPTER.redact(builder.comment);
      if (builder.count != null) builder.count = Count.ADAPTER.redact(builder.count);
      if (builder.flag != null) builder.flag = Flag.ADAPTER.redact(builder.flag);
      Internal.redactElements(builder.subModels, Model.ADAPTER);
      if (builder.picture != null) builder.picture = Picture.ADAPTER.redact(builder.picture);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
