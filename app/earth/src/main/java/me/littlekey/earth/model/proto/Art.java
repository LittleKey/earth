// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ehentai/model.proto at 8:1
package me.littlekey.earth.model.proto;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

public final class Art extends Message<Art, Art.Builder> {
  public static final ProtoAdapter<Art> ADAPTER = new ProtoAdapter_Art();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_TITLE = "";

  public static final String DEFAULT_PUBLISHER_NAME = "";

  public static final Integer DEFAULT_CATEGORY = 0;

  public static final String DEFAULT_DATE = "";

  public static final String DEFAULT_URL = "";

  public static final String DEFAULT_COVER = "";

  public static final String DEFAULT_LANGUAGE = "";

  public static final Boolean DEFAULT_LIKED = false;

  public static final String DEFAULT_FILE_SIZE = "";

  public static final String DEFAULT_GID = "";

  public static final String DEFAULT_TOKEN = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String title;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String publisher_name;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer category;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String date;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String url;

  @WireField(
      tag = 6,
      adapter = "me.littlekey.earth.model.proto.Count#ADAPTER"
  )
  public final Count count;

  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String cover;

  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String language;

  @WireField(
      tag = 9,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean liked;

  @WireField(
      tag = 10,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String file_size;

  @WireField(
      tag = 11,
      adapter = "me.littlekey.earth.model.proto.Tag#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<Tag> tags;

  @WireField(
      tag = 12,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String gid;

  @WireField(
      tag = 13,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String token;

  @WireField(
      tag = 14,
      adapter = "me.littlekey.earth.model.proto.Comment#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<Comment> comments;

  public Art(String title, String publisher_name, Integer category, String date, String url, Count count, String cover, String language, Boolean liked, String file_size, List<Tag> tags, String gid, String token, List<Comment> comments) {
    this(title, publisher_name, category, date, url, count, cover, language, liked, file_size, tags, gid, token, comments, ByteString.EMPTY);
  }

  public Art(String title, String publisher_name, Integer category, String date, String url, Count count, String cover, String language, Boolean liked, String file_size, List<Tag> tags, String gid, String token, List<Comment> comments, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.title = title;
    this.publisher_name = publisher_name;
    this.category = category;
    this.date = date;
    this.url = url;
    this.count = count;
    this.cover = cover;
    this.language = language;
    this.liked = liked;
    this.file_size = file_size;
    this.tags = Internal.immutableCopyOf("tags", tags);
    this.gid = gid;
    this.token = token;
    this.comments = Internal.immutableCopyOf("comments", comments);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.title = title;
    builder.publisher_name = publisher_name;
    builder.category = category;
    builder.date = date;
    builder.url = url;
    builder.count = count;
    builder.cover = cover;
    builder.language = language;
    builder.liked = liked;
    builder.file_size = file_size;
    builder.tags = Internal.copyOf("tags", tags);
    builder.gid = gid;
    builder.token = token;
    builder.comments = Internal.copyOf("comments", comments);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Art)) return false;
    Art o = (Art) other;
    return Internal.equals(unknownFields(), o.unknownFields())
        && Internal.equals(title, o.title)
        && Internal.equals(publisher_name, o.publisher_name)
        && Internal.equals(category, o.category)
        && Internal.equals(date, o.date)
        && Internal.equals(url, o.url)
        && Internal.equals(count, o.count)
        && Internal.equals(cover, o.cover)
        && Internal.equals(language, o.language)
        && Internal.equals(liked, o.liked)
        && Internal.equals(file_size, o.file_size)
        && Internal.equals(tags, o.tags)
        && Internal.equals(gid, o.gid)
        && Internal.equals(token, o.token)
        && Internal.equals(comments, o.comments);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (publisher_name != null ? publisher_name.hashCode() : 0);
      result = result * 37 + (category != null ? category.hashCode() : 0);
      result = result * 37 + (date != null ? date.hashCode() : 0);
      result = result * 37 + (url != null ? url.hashCode() : 0);
      result = result * 37 + (count != null ? count.hashCode() : 0);
      result = result * 37 + (cover != null ? cover.hashCode() : 0);
      result = result * 37 + (language != null ? language.hashCode() : 0);
      result = result * 37 + (liked != null ? liked.hashCode() : 0);
      result = result * 37 + (file_size != null ? file_size.hashCode() : 0);
      result = result * 37 + (tags != null ? tags.hashCode() : 1);
      result = result * 37 + (gid != null ? gid.hashCode() : 0);
      result = result * 37 + (token != null ? token.hashCode() : 0);
      result = result * 37 + (comments != null ? comments.hashCode() : 1);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (title != null) builder.append(", title=").append(title);
    if (publisher_name != null) builder.append(", publisher_name=").append(publisher_name);
    if (category != null) builder.append(", category=").append(category);
    if (date != null) builder.append(", date=").append(date);
    if (url != null) builder.append(", url=").append(url);
    if (count != null) builder.append(", count=").append(count);
    if (cover != null) builder.append(", cover=").append(cover);
    if (language != null) builder.append(", language=").append(language);
    if (liked != null) builder.append(", liked=").append(liked);
    if (file_size != null) builder.append(", file_size=").append(file_size);
    if (tags != null) builder.append(", tags=").append(tags);
    if (gid != null) builder.append(", gid=").append(gid);
    if (token != null) builder.append(", token=").append(token);
    if (comments != null) builder.append(", comments=").append(comments);
    return builder.replace(0, 2, "Art{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Art, Builder> {
    public String title;

    public String publisher_name;

    public Integer category;

    public String date;

    public String url;

    public Count count;

    public String cover;

    public String language;

    public Boolean liked;

    public String file_size;

    public List<Tag> tags;

    public String gid;

    public String token;

    public List<Comment> comments;

    public Builder() {
      tags = Internal.newMutableList();
      comments = Internal.newMutableList();
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder publisher_name(String publisher_name) {
      this.publisher_name = publisher_name;
      return this;
    }

    public Builder category(Integer category) {
      this.category = category;
      return this;
    }

    public Builder date(String date) {
      this.date = date;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder count(Count count) {
      this.count = count;
      return this;
    }

    public Builder cover(String cover) {
      this.cover = cover;
      return this;
    }

    public Builder language(String language) {
      this.language = language;
      return this;
    }

    public Builder liked(Boolean liked) {
      this.liked = liked;
      return this;
    }

    public Builder file_size(String file_size) {
      this.file_size = file_size;
      return this;
    }

    public Builder tags(List<Tag> tags) {
      Internal.checkElementsNotNull(tags);
      this.tags = tags;
      return this;
    }

    public Builder gid(String gid) {
      this.gid = gid;
      return this;
    }

    public Builder token(String token) {
      this.token = token;
      return this;
    }

    public Builder comments(List<Comment> comments) {
      Internal.checkElementsNotNull(comments);
      this.comments = comments;
      return this;
    }

    @Override
    public Art build() {
      return new Art(title, publisher_name, category, date, url, count, cover, language, liked, file_size, tags, gid, token, comments, buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Art extends ProtoAdapter<Art> {
    ProtoAdapter_Art() {
      super(FieldEncoding.LENGTH_DELIMITED, Art.class);
    }

    @Override
    public int encodedSize(Art value) {
      return (value.title != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.title) : 0)
          + (value.publisher_name != null ? ProtoAdapter.STRING.encodedSizeWithTag(2, value.publisher_name) : 0)
          + (value.category != null ? ProtoAdapter.INT32.encodedSizeWithTag(3, value.category) : 0)
          + (value.date != null ? ProtoAdapter.STRING.encodedSizeWithTag(4, value.date) : 0)
          + (value.url != null ? ProtoAdapter.STRING.encodedSizeWithTag(5, value.url) : 0)
          + (value.count != null ? Count.ADAPTER.encodedSizeWithTag(6, value.count) : 0)
          + (value.cover != null ? ProtoAdapter.STRING.encodedSizeWithTag(7, value.cover) : 0)
          + (value.language != null ? ProtoAdapter.STRING.encodedSizeWithTag(8, value.language) : 0)
          + (value.liked != null ? ProtoAdapter.BOOL.encodedSizeWithTag(9, value.liked) : 0)
          + (value.file_size != null ? ProtoAdapter.STRING.encodedSizeWithTag(10, value.file_size) : 0)
          + Tag.ADAPTER.asRepeated().encodedSizeWithTag(11, value.tags)
          + (value.gid != null ? ProtoAdapter.STRING.encodedSizeWithTag(12, value.gid) : 0)
          + (value.token != null ? ProtoAdapter.STRING.encodedSizeWithTag(13, value.token) : 0)
          + Comment.ADAPTER.asRepeated().encodedSizeWithTag(14, value.comments)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Art value) throws IOException {
      if (value.title != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.title);
      if (value.publisher_name != null) ProtoAdapter.STRING.encodeWithTag(writer, 2, value.publisher_name);
      if (value.category != null) ProtoAdapter.INT32.encodeWithTag(writer, 3, value.category);
      if (value.date != null) ProtoAdapter.STRING.encodeWithTag(writer, 4, value.date);
      if (value.url != null) ProtoAdapter.STRING.encodeWithTag(writer, 5, value.url);
      if (value.count != null) Count.ADAPTER.encodeWithTag(writer, 6, value.count);
      if (value.cover != null) ProtoAdapter.STRING.encodeWithTag(writer, 7, value.cover);
      if (value.language != null) ProtoAdapter.STRING.encodeWithTag(writer, 8, value.language);
      if (value.liked != null) ProtoAdapter.BOOL.encodeWithTag(writer, 9, value.liked);
      if (value.file_size != null) ProtoAdapter.STRING.encodeWithTag(writer, 10, value.file_size);
      if (value.tags != null) Tag.ADAPTER.asRepeated().encodeWithTag(writer, 11, value.tags);
      if (value.gid != null) ProtoAdapter.STRING.encodeWithTag(writer, 12, value.gid);
      if (value.token != null) ProtoAdapter.STRING.encodeWithTag(writer, 13, value.token);
      if (value.comments != null) Comment.ADAPTER.asRepeated().encodeWithTag(writer, 14, value.comments);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Art decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.title(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.publisher_name(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.category(ProtoAdapter.INT32.decode(reader)); break;
          case 4: builder.date(ProtoAdapter.STRING.decode(reader)); break;
          case 5: builder.url(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.count(Count.ADAPTER.decode(reader)); break;
          case 7: builder.cover(ProtoAdapter.STRING.decode(reader)); break;
          case 8: builder.language(ProtoAdapter.STRING.decode(reader)); break;
          case 9: builder.liked(ProtoAdapter.BOOL.decode(reader)); break;
          case 10: builder.file_size(ProtoAdapter.STRING.decode(reader)); break;
          case 11: builder.tags.add(Tag.ADAPTER.decode(reader)); break;
          case 12: builder.gid(ProtoAdapter.STRING.decode(reader)); break;
          case 13: builder.token(ProtoAdapter.STRING.decode(reader)); break;
          case 14: builder.comments.add(Comment.ADAPTER.decode(reader)); break;
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
    public Art redact(Art value) {
      Builder builder = value.newBuilder();
      if (builder.count != null) builder.count = Count.ADAPTER.redact(builder.count);
      Internal.redactElements(builder.tags, Tag.ADAPTER);
      Internal.redactElements(builder.comments, Comment.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
