// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./earth.proto at 6:1
package me.littlekey.earth.model.proto;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class Count extends Message<Count, Count.Builder> {
  public static final ProtoAdapter<Count> ADAPTER = new ProtoAdapter_Count();

  private static final long serialVersionUID = 0L;

  public static final Float DEFAULT_RATING = 0.0f;

  public static final Integer DEFAULT_SELECTED_NUM = 0;

  public static final Integer DEFAULT_LIKES = 0;

  public static final Integer DEFAULT_PAGES = 0;

  public static final Integer DEFAULT_RATING_COUNT = 0;

  public static final Integer DEFAULT_NUMBER = 0;

  public static final Integer DEFAULT_WIDTH = 0;

  public static final Integer DEFAULT_HEIGHT = 0;

  public static final Integer DEFAULT_X_OFFSET = 0;

  public static final Integer DEFAULT_SCORE = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  public final Float rating;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer selected_num;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer likes;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer pages;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer rating_count;

  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer number;

  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer width;

  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer height;

  @WireField(
      tag = 9,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer x_offset;

  @WireField(
      tag = 10,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer score;

  public Count(Float rating, Integer selected_num, Integer likes, Integer pages, Integer rating_count, Integer number, Integer width, Integer height, Integer x_offset, Integer score) {
    this(rating, selected_num, likes, pages, rating_count, number, width, height, x_offset, score, ByteString.EMPTY);
  }

  public Count(Float rating, Integer selected_num, Integer likes, Integer pages, Integer rating_count, Integer number, Integer width, Integer height, Integer x_offset, Integer score, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.rating = rating;
    this.selected_num = selected_num;
    this.likes = likes;
    this.pages = pages;
    this.rating_count = rating_count;
    this.number = number;
    this.width = width;
    this.height = height;
    this.x_offset = x_offset;
    this.score = score;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.rating = rating;
    builder.selected_num = selected_num;
    builder.likes = likes;
    builder.pages = pages;
    builder.rating_count = rating_count;
    builder.number = number;
    builder.width = width;
    builder.height = height;
    builder.x_offset = x_offset;
    builder.score = score;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Count)) return false;
    Count o = (Count) other;
    return Internal.equals(unknownFields(), o.unknownFields())
        && Internal.equals(rating, o.rating)
        && Internal.equals(selected_num, o.selected_num)
        && Internal.equals(likes, o.likes)
        && Internal.equals(pages, o.pages)
        && Internal.equals(rating_count, o.rating_count)
        && Internal.equals(number, o.number)
        && Internal.equals(width, o.width)
        && Internal.equals(height, o.height)
        && Internal.equals(x_offset, o.x_offset)
        && Internal.equals(score, o.score);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (rating != null ? rating.hashCode() : 0);
      result = result * 37 + (selected_num != null ? selected_num.hashCode() : 0);
      result = result * 37 + (likes != null ? likes.hashCode() : 0);
      result = result * 37 + (pages != null ? pages.hashCode() : 0);
      result = result * 37 + (rating_count != null ? rating_count.hashCode() : 0);
      result = result * 37 + (number != null ? number.hashCode() : 0);
      result = result * 37 + (width != null ? width.hashCode() : 0);
      result = result * 37 + (height != null ? height.hashCode() : 0);
      result = result * 37 + (x_offset != null ? x_offset.hashCode() : 0);
      result = result * 37 + (score != null ? score.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (rating != null) builder.append(", rating=").append(rating);
    if (selected_num != null) builder.append(", selected_num=").append(selected_num);
    if (likes != null) builder.append(", likes=").append(likes);
    if (pages != null) builder.append(", pages=").append(pages);
    if (rating_count != null) builder.append(", rating_count=").append(rating_count);
    if (number != null) builder.append(", number=").append(number);
    if (width != null) builder.append(", width=").append(width);
    if (height != null) builder.append(", height=").append(height);
    if (x_offset != null) builder.append(", x_offset=").append(x_offset);
    if (score != null) builder.append(", score=").append(score);
    return builder.replace(0, 2, "Count{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Count, Builder> {
    public Float rating;

    public Integer selected_num;

    public Integer likes;

    public Integer pages;

    public Integer rating_count;

    public Integer number;

    public Integer width;

    public Integer height;

    public Integer x_offset;

    public Integer score;

    public Builder() {
    }

    public Builder rating(Float rating) {
      this.rating = rating;
      return this;
    }

    public Builder selected_num(Integer selected_num) {
      this.selected_num = selected_num;
      return this;
    }

    public Builder likes(Integer likes) {
      this.likes = likes;
      return this;
    }

    public Builder pages(Integer pages) {
      this.pages = pages;
      return this;
    }

    public Builder rating_count(Integer rating_count) {
      this.rating_count = rating_count;
      return this;
    }

    public Builder number(Integer number) {
      this.number = number;
      return this;
    }

    public Builder width(Integer width) {
      this.width = width;
      return this;
    }

    public Builder height(Integer height) {
      this.height = height;
      return this;
    }

    public Builder x_offset(Integer x_offset) {
      this.x_offset = x_offset;
      return this;
    }

    public Builder score(Integer score) {
      this.score = score;
      return this;
    }

    @Override
    public Count build() {
      return new Count(rating, selected_num, likes, pages, rating_count, number, width, height, x_offset, score, buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Count extends ProtoAdapter<Count> {
    ProtoAdapter_Count() {
      super(FieldEncoding.LENGTH_DELIMITED, Count.class);
    }

    @Override
    public int encodedSize(Count value) {
      return (value.rating != null ? ProtoAdapter.FLOAT.encodedSizeWithTag(1, value.rating) : 0)
          + (value.selected_num != null ? ProtoAdapter.UINT32.encodedSizeWithTag(2, value.selected_num) : 0)
          + (value.likes != null ? ProtoAdapter.UINT32.encodedSizeWithTag(3, value.likes) : 0)
          + (value.pages != null ? ProtoAdapter.UINT32.encodedSizeWithTag(4, value.pages) : 0)
          + (value.rating_count != null ? ProtoAdapter.UINT32.encodedSizeWithTag(5, value.rating_count) : 0)
          + (value.number != null ? ProtoAdapter.UINT32.encodedSizeWithTag(6, value.number) : 0)
          + (value.width != null ? ProtoAdapter.UINT32.encodedSizeWithTag(7, value.width) : 0)
          + (value.height != null ? ProtoAdapter.UINT32.encodedSizeWithTag(8, value.height) : 0)
          + (value.x_offset != null ? ProtoAdapter.INT32.encodedSizeWithTag(9, value.x_offset) : 0)
          + (value.score != null ? ProtoAdapter.INT32.encodedSizeWithTag(10, value.score) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Count value) throws IOException {
      if (value.rating != null) ProtoAdapter.FLOAT.encodeWithTag(writer, 1, value.rating);
      if (value.selected_num != null) ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.selected_num);
      if (value.likes != null) ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.likes);
      if (value.pages != null) ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.pages);
      if (value.rating_count != null) ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.rating_count);
      if (value.number != null) ProtoAdapter.UINT32.encodeWithTag(writer, 6, value.number);
      if (value.width != null) ProtoAdapter.UINT32.encodeWithTag(writer, 7, value.width);
      if (value.height != null) ProtoAdapter.UINT32.encodeWithTag(writer, 8, value.height);
      if (value.x_offset != null) ProtoAdapter.INT32.encodeWithTag(writer, 9, value.x_offset);
      if (value.score != null) ProtoAdapter.INT32.encodeWithTag(writer, 10, value.score);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Count decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.rating(ProtoAdapter.FLOAT.decode(reader)); break;
          case 2: builder.selected_num(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.likes(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.pages(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.rating_count(ProtoAdapter.UINT32.decode(reader)); break;
          case 6: builder.number(ProtoAdapter.UINT32.decode(reader)); break;
          case 7: builder.width(ProtoAdapter.UINT32.decode(reader)); break;
          case 8: builder.height(ProtoAdapter.UINT32.decode(reader)); break;
          case 9: builder.x_offset(ProtoAdapter.INT32.decode(reader)); break;
          case 10: builder.score(ProtoAdapter.INT32.decode(reader)); break;
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
    public Count redact(Count value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
