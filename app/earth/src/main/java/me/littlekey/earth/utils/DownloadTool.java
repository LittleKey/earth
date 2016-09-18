package me.littlekey.earth.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.util.SparseArray;

import me.littlekey.base.utils.DeviceConfig;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import timber.log.Timber;

/**
 * Created by littlekey on 16/7/4.
 */
public class DownloadTool {
  // FILE Utils
  public static final int S_IRWXU = 00700;
  public static final int S_IRUSR = 00400;
  public static final int S_IWUSR = 00200;
  public static final int S_IXUSR = 00100;

  public static final int S_IRWXG = 00070;
  public static final int S_IRGRP = 00040;
  public static final int S_IWGRP = 00020;
  public static final int S_IXGRP = 00010;

  public static final int S_IRWXO = 00007;
  public static final int S_IROTH = 00004;
  public static final int S_IWOTH = 00002;
  public static final int S_IXOTH = 00001;

  public static final String DOWNLOAD_PATH = "/download";

  public static boolean isInDownloadList(Model item, Map<Model, Messenger> clients, Messenger replyTo) {
    if (clients != null && clients.containsKey(item)) {
      clients.put(item, replyTo);
      return true;
    }
    return false;
  }

  public static int buildNotificationID(Model model) {
    return model.addition.identity.hashCode() + model.addition.token.hashCode();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static File getDownloadDir(Context context, boolean[] isSdCard) {
    File file;
    String root;
    if (DeviceConfig.isSdCardWritable() && context.getExternalCacheDir() != null) {
      root = context.getExternalCacheDir().getAbsolutePath();
      root += DOWNLOAD_PATH;
      file = new File(root);
      file.mkdirs();
      if (file.exists()) {
        isSdCard[0] = true;
        return file;
      }
    }
    root = context.getCacheDir().getAbsolutePath();
    new File(root).mkdir();
    setPermissions(root, S_IRWXU | S_IRWXG | S_IXOTH, -1, -1);
    root += DOWNLOAD_PATH;
    new File(root).mkdir();
    setPermissions(root, S_IRWXU | S_IRWXG | S_IXOTH, -1, -1);
    file = new File(root);
    isSdCard[0] = false;
    return file;
  }


  @SuppressWarnings("deprecation")
  public static boolean setFilePermissionsFromMode(String name, int mode) {
    int perms = S_IRUSR | S_IWUSR
        | S_IRGRP | S_IWGRP;
    if ((mode & Context.MODE_WORLD_READABLE) != 0) {
      perms |= S_IROTH;
    }
    if ((mode & Context.MODE_WORLD_WRITEABLE) != 0) {
      perms |= S_IWOTH;
    }
    return setPermissions(name, perms, -1, -1);
  }

  private static boolean setPermissions(String file, int mode, int uid, int gid) {
    try {
      Class<?> class1 = Class.forName("android.os.FileUtils");
      Method method =
          class1.getMethod("setPermissions", String.class, int.class, int.class, int.class);
      method.invoke(null, file, mode, uid, gid);
      return true;
    } catch (Exception e) {
      Timber.e("error when set permissions:", e);
    }
    return false;
  }

  public static NotificationCompat.Builder getNotification(Context context, Model model, int prePos) {
    context = context.getApplicationContext();
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
    PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
        new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
    builder.setTicker(context.getString(R.string.start_download_notification))
        .setSmallIcon(android.R.drawable.stat_sys_download)
        .setContentIntent(contentIntent)
        .setWhen(System.currentTimeMillis())
        .setContentTitle(
            context.getString(R.string.download_notification_prefix) + model.addition.title)
        .setProgress(100, prePos, false);
    return builder;
  }

  public static void clearCache(NotificationManager notificationManager, SparseArray<Pair> pairCache,
      Map<Model, Messenger> clients, int nid) {
    Pair pair = pairCache.get(nid);
    if (pair != null) {
      notificationManager.cancel(nid);
      if (clients.containsKey(pair.model)) {
        clients.remove(pair.model);
      }
      pair.dumpCache(pairCache);
    }
  }


  public static class Pair {
    public NotificationCompat.Builder currentNotificationBuilder;
    public final int nid;
    public final Model model;

    public Pair(Model model, int nid) {
      super();
      this.nid = nid;
      this.model = model;
    }

    public void pushToCache(SparseArray<Pair> pairCache) {
      pairCache.put(nid, this);
    }

    public void dumpCache(SparseArray<Pair> pairCache) {
      if (pairCache.indexOfKey(nid) >= 0)
        pairCache.remove(nid);
    }
  }
}
