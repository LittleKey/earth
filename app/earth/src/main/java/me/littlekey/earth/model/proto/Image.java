// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ehentai/model.proto at 28:1
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

public final class Image extends Message<Image, Image.Builder> {
  public static final ProtoAdapter<Image> ADAPTER = new ProtoAdapter_Image();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_THUMBNAIL = "";

  public static final String DEFAULT_NORMAL = "";

  public static final String DEFAULT_ORIGIN = "";

  public static final Integer DEFAULT_NUMBER = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String thumbnail;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String normal;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String origin;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer number;

  public Image(String thumbnail, String normal, String origin, Integer number) {
    this(thumbnail, normal, origin, number, ByteString.EMPTY);
  }

  public Image(String thumbnail, String normal, String origin, Integer number, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.thumbnail = thumbnail;
    this.normal = normal;
    this.origin = origin;
    this.number = number;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.thumbnail = thumbnail;
    builder.normal = normal;
    builder.origin = origin;
    builder.number = number;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Image)) return false;
    Image o = (Image) other;
    return Internal.equals(unknownFields(), o.unknownFields())
        && Internal.equals(thumbnail, o.thumbnail)
        && Internal.equals(normal, o.normal)
        && Internal.equals(origin, o.origin)
        && Internal.equals(number, o.number);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (thumbnail != null ? thumbnail.hashCode() : 0);
      result = result * 37 + (normal != null ? normal.hashCode() : 0);
      result = result * 37 + (origin != null ? origin.hashCode() : 0);
      result = result * 37 + (number != null ? number.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (thumbnail != null) builder.append(", thumbnail=").append(thumbnail);
    if (normal != null) builder.append(", normal=").append(normal);
    if (origin != null) builder.append(", origin=").append(origin);
    if (number != null) builder.append(", number=").append(number);
    return builder.replace(0, 2, "Image{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Image, Builder> {
    public String thumbnail;

    public String normal;

    public String origin;

    public Integer number;

    public Builder() {
    }

    public Builder thumbnail(String thumbnail) {
      this.thumbnail = thumbnail;
      return this;
    }

    public Builder normal(String normal) {
      this.normal = normal;
      return this;
    }

    public Builder origin(String origin) {
      this.origin = origin;
      return this;
    }

    public Builder number(Integer number) {
      this.number = number;
      return this;
    }

    @Override
    public Image build() {
      return new Image(thumbnail, normal, origin, number, buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Image extends ProtoAdapter<Image> {
    ProtoAdapter_Image() {
      super(FieldEncoding.LENGTH_DELIMITED, Image.class);
    }

    @Override
    public int encodedSize(Image value) {
      return (value.thumbnail != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.thumbnail) : 0)
          + (value.normal != null ? ProtoAdapter.STRING.encodedSizeWithTag(2, value.normal) : 0)
          + (value.origin != null ? ProtoAdapter.STRING.encodedSizeWithTag(3, value.origin) : 0)
          + (value.number != null ? ProtoAdapter.INT32.encodedSizeWithTag(4, value.number) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Image value) throws IOException {
      if (value.thumbnail != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.thumbnail);
      if (value.normal != null) ProtoAdapter.STRING.encodeWithTag(writer, 2, value.normal);
      if (value.origin != null) ProtoAdapter.STRING.encodeWithTag(writer, 3, value.origin);
      if (value.number != null) ProtoAdapter.INT32.encodeWithTag(writer, 4, value.number);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Image decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.thumbnail(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.normal(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.origin(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.number(ProtoAdapter.INT32.decode(reader)); break;
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
    public Image redact(Image value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
