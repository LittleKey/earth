// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ehentai/model.proto at 54:1
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

public final class Fav extends Message<Fav, Fav.Builder> {
  public static final ProtoAdapter<Fav> ADAPTER = new ProtoAdapter_Fav();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_ID = "";

  public static final String DEFAULT_NAME = "";

  public static final String DEFAULT_APPLY = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String id;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String name;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String apply;

  public Fav(String id, String name, String apply) {
    this(id, name, apply, ByteString.EMPTY);
  }

  public Fav(String id, String name, String apply, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.id = id;
    this.name = name;
    this.apply = apply;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.id = id;
    builder.name = name;
    builder.apply = apply;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Fav)) return false;
    Fav o = (Fav) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(id, o.id)
        && Internal.equals(name, o.name)
        && Internal.equals(apply, o.apply);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (id != null ? id.hashCode() : 0);
      result = result * 37 + (name != null ? name.hashCode() : 0);
      result = result * 37 + (apply != null ? apply.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (id != null) builder.append(", id=").append(id);
    if (name != null) builder.append(", name=").append(name);
    if (apply != null) builder.append(", apply=").append(apply);
    return builder.replace(0, 2, "Fav{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Fav, Builder> {
    public String id;

    public String name;

    public String apply;

    public Builder() {
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder apply(String apply) {
      this.apply = apply;
      return this;
    }

    @Override
    public Fav build() {
      return new Fav(id, name, apply, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Fav extends ProtoAdapter<Fav> {
    ProtoAdapter_Fav() {
      super(FieldEncoding.LENGTH_DELIMITED, Fav.class);
    }

    @Override
    public int encodedSize(Fav value) {
      return (value.id != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.id) : 0)
          + (value.name != null ? ProtoAdapter.STRING.encodedSizeWithTag(2, value.name) : 0)
          + (value.apply != null ? ProtoAdapter.STRING.encodedSizeWithTag(3, value.apply) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Fav value) throws IOException {
      if (value.id != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.id);
      if (value.name != null) ProtoAdapter.STRING.encodeWithTag(writer, 2, value.name);
      if (value.apply != null) ProtoAdapter.STRING.encodeWithTag(writer, 3, value.apply);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Fav decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.id(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.name(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.apply(ProtoAdapter.STRING.decode(reader)); break;
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
    public Fav redact(Fav value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
