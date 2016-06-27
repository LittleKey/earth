// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./earth.proto at 44:1
package me.littlekey.earth.model.proto;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireEnum;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class User extends Message<User, User.Builder> {
  public static final ProtoAdapter<User> ADAPTER = new ProtoAdapter_User();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_USER_ID = "";

  public static final String DEFAULT_DISPLAY_NAME = "";

  public static final String DEFAULT_IMAGE = "";

  public static final String DEFAULT_BIO = "";

  public static final Gender DEFAULT_GENDER = Gender.UNKNOWN;

  public static final Boolean DEFAULT_REJECT_INVITATION = false;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String user_id;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String display_name;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String image;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String bio;

  @WireField(
      tag = 9,
      adapter = "me.littlekey.earth.model.proto.User$Gender#ADAPTER"
  )
  public final Gender gender;

  @WireField(
      tag = 13,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean reject_invitation;

  public User(String user_id, String display_name, String image, String bio, Gender gender, Boolean reject_invitation) {
    this(user_id, display_name, image, bio, gender, reject_invitation, ByteString.EMPTY);
  }

  public User(String user_id, String display_name, String image, String bio, Gender gender, Boolean reject_invitation, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.user_id = user_id;
    this.display_name = display_name;
    this.image = image;
    this.bio = bio;
    this.gender = gender;
    this.reject_invitation = reject_invitation;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.user_id = user_id;
    builder.display_name = display_name;
    builder.image = image;
    builder.bio = bio;
    builder.gender = gender;
    builder.reject_invitation = reject_invitation;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof User)) return false;
    User o = (User) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(user_id, o.user_id)
        && Internal.equals(display_name, o.display_name)
        && Internal.equals(image, o.image)
        && Internal.equals(bio, o.bio)
        && Internal.equals(gender, o.gender)
        && Internal.equals(reject_invitation, o.reject_invitation);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (user_id != null ? user_id.hashCode() : 0);
      result = result * 37 + (display_name != null ? display_name.hashCode() : 0);
      result = result * 37 + (image != null ? image.hashCode() : 0);
      result = result * 37 + (bio != null ? bio.hashCode() : 0);
      result = result * 37 + (gender != null ? gender.hashCode() : 0);
      result = result * 37 + (reject_invitation != null ? reject_invitation.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (user_id != null) builder.append(", user_id=").append(user_id);
    if (display_name != null) builder.append(", display_name=").append(display_name);
    if (image != null) builder.append(", image=").append(image);
    if (bio != null) builder.append(", bio=").append(bio);
    if (gender != null) builder.append(", gender=").append(gender);
    if (reject_invitation != null) builder.append(", reject_invitation=").append(reject_invitation);
    return builder.replace(0, 2, "User{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<User, Builder> {
    public String user_id;

    public String display_name;

    public String image;

    public String bio;

    public Gender gender;

    public Boolean reject_invitation;

    public Builder() {
    }

    public Builder user_id(String user_id) {
      this.user_id = user_id;
      return this;
    }

    public Builder display_name(String display_name) {
      this.display_name = display_name;
      return this;
    }

    public Builder image(String image) {
      this.image = image;
      return this;
    }

    public Builder bio(String bio) {
      this.bio = bio;
      return this;
    }

    public Builder gender(Gender gender) {
      this.gender = gender;
      return this;
    }

    public Builder reject_invitation(Boolean reject_invitation) {
      this.reject_invitation = reject_invitation;
      return this;
    }

    @Override
    public User build() {
      return new User(user_id, display_name, image, bio, gender, reject_invitation, super.buildUnknownFields());
    }
  }

  public enum Gender implements WireEnum {
    UNKNOWN(0),

    MALE(1),

    FEMALE(2);

    public static final ProtoAdapter<Gender> ADAPTER = ProtoAdapter.newEnumAdapter(Gender.class);

    private final int value;

    Gender(int value) {
      this.value = value;
    }

    /**
     * Return the constant for {@code value} or null.
     */
    public static Gender fromValue(int value) {
      switch (value) {
        case 0: return UNKNOWN;
        case 1: return MALE;
        case 2: return FEMALE;
        default: return null;
      }
    }

    @Override
    public int getValue() {
      return value;
    }
  }

  private static final class ProtoAdapter_User extends ProtoAdapter<User> {
    ProtoAdapter_User() {
      super(FieldEncoding.LENGTH_DELIMITED, User.class);
    }

    @Override
    public int encodedSize(User value) {
      return (value.user_id != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.user_id) : 0)
          + (value.display_name != null ? ProtoAdapter.STRING.encodedSizeWithTag(3, value.display_name) : 0)
          + (value.image != null ? ProtoAdapter.STRING.encodedSizeWithTag(4, value.image) : 0)
          + (value.bio != null ? ProtoAdapter.STRING.encodedSizeWithTag(5, value.bio) : 0)
          + (value.gender != null ? Gender.ADAPTER.encodedSizeWithTag(9, value.gender) : 0)
          + (value.reject_invitation != null ? ProtoAdapter.BOOL.encodedSizeWithTag(13, value.reject_invitation) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, User value) throws IOException {
      if (value.user_id != null) ProtoAdapter.STRING.encodeWithTag(writer, 1, value.user_id);
      if (value.display_name != null) ProtoAdapter.STRING.encodeWithTag(writer, 3, value.display_name);
      if (value.image != null) ProtoAdapter.STRING.encodeWithTag(writer, 4, value.image);
      if (value.bio != null) ProtoAdapter.STRING.encodeWithTag(writer, 5, value.bio);
      if (value.gender != null) Gender.ADAPTER.encodeWithTag(writer, 9, value.gender);
      if (value.reject_invitation != null) ProtoAdapter.BOOL.encodeWithTag(writer, 13, value.reject_invitation);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public User decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.user_id(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.display_name(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.image(ProtoAdapter.STRING.decode(reader)); break;
          case 5: builder.bio(ProtoAdapter.STRING.decode(reader)); break;
          case 9: {
            try {
              builder.gender(Gender.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 13: builder.reject_invitation(ProtoAdapter.BOOL.decode(reader)); break;
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
    public User redact(User value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
