# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/littlekey/Downloads/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings
#-dontshrink
-dontoptimize
-dontpreverify
#-dontobfuscate
-printseeds

-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes Signature

-assumenosideeffects class android.util.Log{
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
    public static boolean isLoggable(java.lang.String, int);
}

-assumenosideeffects class * extends java.lang.Throwable {
    public void printStackTrace();
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class * extends android.os.IInterface
-keep class android.media.* { *; }
-keep public class com.android.internal.telephony.* { *; }
-keep public class android.os.storage.* { *; }
-keep public class android.content.pm.* { *; }
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class android.support.design.** { *; }
-keep public class android.support.v7.** { *; }
-keep class javax.annotation.** { *; }
-keep class me.littlekey.earth.event.* { *; }

-keep class com.yuanqi.basket.model.proto.** { *; }
-keep class com.yuanqi.basket.model.business.** { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class * implements java.io.Serializable {
    *;
}

-keep class *.R

-keepclasseswithmembers class **.R$* {
    public static <fields>;
}

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class android.net.http.SslError{ *; }
-keep public class android.webkit.WebViewClient{ *; }
-keep public class android.webkit.WebChromeClient{ *; }
-keep public interface android.webkit.WebChromeClient$CustomViewCallback { *; }
-keep public interface android.webkit.ValueCallback { *; }
-keep class * implements android.webkit.WebChromeClient { *; }
-keep class * extends com.squareup.wire.Message { *; }
-keep class * extends com.squareup.wire.Message$Builder { *; }
-dontwarn android.support.**
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
# keep sina sdk
-keep class com.sina.** { *; }
# keep eventbus
-keepclassmembers class ** {
    public void onEvent*(***);
}
# keep ijkplayer
-keep class tv.danmaku.ijk.** { *; }
#####################
# Umeng
#####################
-keep class * extends com.umeng.**
-keep class com.umeng.** { *; }
-keep class org.apache.http.** { *; }
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**


-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

#####################
# JPush
#####################
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}

#==================hupu==========================
-keep class com.hupu.statistics.** {*;}
-dontwarn com.hupu.statistics.**