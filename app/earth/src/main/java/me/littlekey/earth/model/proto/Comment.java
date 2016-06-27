// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ehentai/model.proto at 25:1
package me.littlekey.earth.model.proto;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class Comment extends Message<Comment, Comment.Builder> {
  public static final ProtoAdapter<Comment> ADAPTER = new ProtoAdapter_Comment();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_AUTHOR = "";

  public static final String DEFAULT_DATE = "";

  public static final String DEFAULT_CONTENT = "";

  public static final Integer DEFAULT_SCORE = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String author;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String date;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String content;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer score;

  public Comment(String author, String date, String content, Integer score) {
    this(author, date, content, score, ByteString.EMPTY);
  }

  public Comment(String author, String date, String content, Integer score, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.author = author;
    this.date = date;
    this.content = content;
    this.score = score;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.author = author;
    builder.date = date;
    builder.content = content;
    builder.score = score;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Comment)) return false;
    Comment o = (Comment) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(author, o.author)
        && Internal.equals(date, o.date)
        && Internal.equals(content, o.content)
        && Internal.equals(score, o.score);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (author != null ? author.hashCode() : 0);
      result = result * 37 + (date != null ? date.hashCode() : 0);
      result = result * 37 + (content != null ? content.hashCode() : 0);
      result = result * 37 + (score != null ? score.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (author != null) builder.append(", author=").append(author);
    if (date != null) builder.append(", date=").append(date);
    if (content != null) builder.append(", content=").append(content);
    if (score != null) builder.append(", score=").append(score);
    return builder.replace(0, 2, "Comment{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Comment, Builder> {
    public String author;

    public String date;

    public String content;

    public Integer score;

    public Builder() {
    }

    public Builder author(String author) {
      this.author = author;
      return this;
    }

    public Builder date(String date) {
      this.date = date;
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Builder score(Integer score) {
      this.score = score;
      return this;
    }

    @Override
    public Comment build() {
      return new Comment(author, date, content, score, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Comment extends ProtoAdapter<Comment> {
    ProtoAdapter_Comment() {
      super(FieldEncoding.LENGTH_DELIMITED, Comment.class);
    }

    @Override
    public int encodedSize(Comment value) {
      return (value.author != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.author) : 0)
          + (value.date != null ? ProtoAdapter.STRING.encodedSizeWithTag(2, value.date) : 0)
          + (value.content != null ? ProtoAdapter.STRING.encodedSizeWithTag(3, value.content) : 0)
          + (value.score != null ? ProtoAdapter.INT32.encodedSizeWithTag(4, value.score) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Comment value) throws IOException {
      if (value.author != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.author);
      if (value.date != null) ProtoAdapter.STRING.encodeWithTag(writer, 2, value.date);
      if (value.content != null) ProtoAdapter.STRING.encodeWithTag(writer, 3, value.content);
      if (value.score != null) ProtoAdapter.INT32.encodeWithTag(writer, 4, value.score);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Comment decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.author(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.date(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.content(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.score(ProtoAdapter.INT32.decode(reader)); break;
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
    public Comment redact(Comment value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}