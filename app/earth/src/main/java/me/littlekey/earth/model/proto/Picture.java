// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ehentai/model.proto at 60:1
package me.littlekey.earth.model.proto;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class Picture extends Message<Picture, Picture.Builder> {
  public static final ProtoAdapter<Picture> ADAPTER = new ProtoAdapter_Picture();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_NAME = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String name;

  @WireField(
      tag = 2,
      adapter = "me.littlekey.earth.model.proto.Image#ADAPTER"
  )
  public final Image image;

  public Picture(String name, Image image) {
    this(name, image, ByteString.EMPTY);
  }

  public Picture(String name, Image image, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.name = name;
    this.image = image;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.name = name;
    builder.image = image;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Picture)) return false;
    Picture o = (Picture) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(name, o.name)
        && Internal.equals(image, o.image);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (name != null ? name.hashCode() : 0);
      result = result * 37 + (image != null ? image.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (name != null) builder.append(", name=").append(name);
    if (image != null) builder.append(", image=").append(image);
    return builder.replace(0, 2, "Picture{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Picture, Builder> {
    public String name;

    public Image image;

    public Builder() {
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder image(Image image) {
      this.image = image;
      return this;
    }

    @Override
    public Picture build() {
      return new Picture(name, image, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Picture extends ProtoAdapter<Picture> {
    ProtoAdapter_Picture() {
      super(FieldEncoding.LENGTH_DELIMITED, Picture.class);
    }

    @Override
    public int encodedSize(Picture value) {
      return (value.name != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.name) : 0)
          + (value.image != null ? Image.ADAPTER.encodedSizeWithTag(2, value.image) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Picture value) throws IOException {
      if (value.name != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.name);
      if (value.image != null) Image.ADAPTER.encodeWithTag(writer, 2, value.image);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Picture decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.name(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.image(Image.ADAPTER.decode(reader)); break;
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
    public Picture redact(Picture value) {
      Builder builder = value.newBuilder();
      if (builder.image != null) builder.image = Image.ADAPTER.redact(builder.image);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}