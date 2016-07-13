// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: files.proto at 8:1
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
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class GetDownloadFileRequest extends Message<GetDownloadFileRequest, GetDownloadFileRequest.Builder> {
  public static final ProtoAdapter<GetDownloadFileRequest> ADAPTER = new ProtoAdapter_GetDownloadFileRequest();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_TIMESTAMP = 0L;

  public static final Integer DEFAULT_LIMIT = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#INT64"
  )
  public final Long timestamp;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  public final Integer limit;

  public GetDownloadFileRequest(Long timestamp, Integer limit) {
    this(timestamp, limit, ByteString.EMPTY);
  }

  public GetDownloadFileRequest(Long timestamp, Integer limit, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.timestamp = timestamp;
    this.limit = limit;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.timestamp = timestamp;
    builder.limit = limit;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetDownloadFileRequest)) return false;
    GetDownloadFileRequest o = (GetDownloadFileRequest) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(timestamp, o.timestamp)
        && Internal.equals(limit, o.limit);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (timestamp != null ? timestamp.hashCode() : 0);
      result = result * 37 + (limit != null ? limit.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (timestamp != null) builder.append(", timestamp=").append(timestamp);
    if (limit != null) builder.append(", limit=").append(limit);
    return builder.replace(0, 2, "GetDownloadFileRequest{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<GetDownloadFileRequest, Builder> {
    public Long timestamp;

    public Integer limit;

    public Builder() {
    }

    public Builder timestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder limit(Integer limit) {
      this.limit = limit;
      return this;
    }

    @Override
    public GetDownloadFileRequest build() {
      return new GetDownloadFileRequest(timestamp, limit, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GetDownloadFileRequest extends ProtoAdapter<GetDownloadFileRequest> {
    ProtoAdapter_GetDownloadFileRequest() {
      super(FieldEncoding.LENGTH_DELIMITED, GetDownloadFileRequest.class);
    }

    @Override
    public int encodedSize(GetDownloadFileRequest value) {
      return (value.timestamp != null ? ProtoAdapter.INT64.encodedSizeWithTag(1, value.timestamp) : 0)
          + (value.limit != null ? ProtoAdapter.INT32.encodedSizeWithTag(2, value.limit) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GetDownloadFileRequest value) throws IOException {
      if (value.timestamp != null) ProtoAdapter.INT64.encodeWithTag(writer, 1, value.timestamp);
      if (value.limit != null) ProtoAdapter.INT32.encodeWithTag(writer, 2, value.limit);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GetDownloadFileRequest decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.timestamp(ProtoAdapter.INT64.decode(reader)); break;
          case 2: builder.limit(ProtoAdapter.INT32.decode(reader)); break;
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
    public GetDownloadFileRequest redact(GetDownloadFileRequest value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}