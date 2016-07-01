package me.littlekey.earth.model.proto;

import android.net.Uri;
import android.os.Bundle;

/**
 * Created by littlekey on 16/6/10.
 */

public final class Action {

  public static final Type DEFAULT_TYPE = Type.UNKNOWN;
  public static final String DEFAULT_NAME = "";
  public static final String DEFAULT_CONTENT = "";
  public static final String DEFAULT_URI = "";
  public static final String DEFAULT_URL = "";

  public final Type type;

  public final String name;

  public final String content;

  public final Class clazz;

  public final Uri uri;

  public final String url;

  public final Bundle bundle;

  public final Runnable runnable;

  public final String transitionName;

  private Action(Builder builder) {
    this.type = builder.type;
    this.name = builder.name;
    this.content = builder.content;
    this.clazz = builder.clazz;
    this.uri = builder.uri;
    this.url = builder.url;
    this.bundle = builder.bundle;
    this.runnable = builder.runnable;
    this.transitionName = builder.transitionName;
  }

  public static final class Builder {

    public Type type;
    public String name;
    public String content;
    public Class clazz;
    public Uri uri;
    public String url;
    public Bundle bundle;
    public Runnable runnable;
    public String transitionName;

    public Builder() {
    }

    public Builder(Action message) {
      if (message == null) return;
      this.type = message.type;
      this.name = message.name;
      this.content = message.content;
      this.clazz = message.clazz;
      this.uri = message.uri;
      this.url = message.url;
      this.bundle = message.bundle;
      this.runnable = message.runnable;
      this.transitionName = message.transitionName;
    }

    public Builder type(Type type) {
      this.type = type;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Builder clazz(Class clazz) {
      this.clazz = clazz;
      return this;
    }

    public Builder uri(Uri uri) {
      this.uri = uri;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder bundle(Bundle bundle) {
      this.bundle = bundle;
      return this;
    }

    public Builder runnable(Runnable runnable) {
      this.runnable = runnable;
      return this;
    }

    public Builder transitionName(String transitionName) {
      this.transitionName = transitionName;
      return this;
    }

    public Action build() {
      return new Action(this);
    }
  }

  public enum Type {
    UNKNOWN(0),
    JUMP(1),
    JUMP_WITH_LOGIN(2),
    LOGOUT(3),
    RUNNABLE(4),
    SELECT_MEMBER(5),
    NEXT_REGION(6),
    SELECT_REGION(7),
    SHOW_USER_CARD(8),
    SELECT_DAY(9),
    SELECT_STYLE(10),
    SELECT_STATURE(11),
    JUMP_CLEAR_BADGE(12),
    EVENT(13),
    LIKED(14),
    SELECT_FAV(15),
    SELECT_CATEGORY(16),
    JUMP_IMAGE(17);

    private final int value;

    Type(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }
}
