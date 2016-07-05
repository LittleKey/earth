package me.littlekey.earth.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.SparseArray;

import com.yuanqi.base.utils.DeviceConfig;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import me.littlekey.earth.model.Model;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.DownloadThread;
import me.littlekey.earth.utils.DownloadTool;
import me.littlekey.earth.utils.EarthUtils;
import timber.log.Timber;

/**
 * Created by littlekey on 16/7/4.
 */
public class DownloadService extends Service {

  // service handler message
  public static final int MSG_DOWNLOAD = 1;

  // client handler message
  public static final int MSG_START = 10;
  public static final int MSG_COMPLETE = 11;
  public static final int MSG_STATUS = 12;

  // download status
  public static final int DOWNLOAD_STATUS_FAIL = 0;
  public static final int DOWNLOAD_STATUS_SUCCESS = 1;
  public static final int DOWNLOAD_STATUS_DOWNLOADING = 2;
  public static final int DOWNLOAD_STATUS_NO_NETWORK = 3;

  private static final int MAX_REPEAT_COUNT = 3;
  private static final long WAIT_FOR_REPEAT = 8 * 1000;

  private static Map<Model, Messenger> sClients = new HashMap<>();
  private static SparseArray<DownloadTool.Pair> sPairCache = new SparseArray<>();

  private Messenger mMessenger = new Messenger(new ServiceHandler(this));

  private DownloadThread.Listener mDownloadThreadListener;

  public DownloadService() {
    super();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mDownloadThreadListener = new DownloadThread.Listener() {
      @Override
      public void onStart(int nid) {
        if (sPairCache.indexOfKey(nid) >= 0) {
          DownloadTool.Pair pair = sPairCache.get(nid);
          NotificationCompat.Builder builder = DownloadTool.getNotification(
              DownloadService.this, pair.model, 0);
          pair.currentNotificationBuilder = builder;
          startForeground(nid, builder.build());
        }
      }

      @Override
      public void onProgress(int nid, float progress) {
        if (sPairCache.indexOfKey(nid) >= 0) {
          DownloadTool.Pair pair = sPairCache.get(nid);
          NotificationCompat.Builder builder = pair.currentNotificationBuilder;
          builder.setProgress(1000, (int) (progress * 10), false)
              .setContentText(EarthUtils.formatString("%.2f%%", progress));
          startForeground(nid, builder.build());
          Messenger client;
          if ((client = sClients.get(pair.model)) != null) {
            Message msg = Message.obtain(null, MSG_STATUS, DOWNLOAD_STATUS_DOWNLOADING, 0, progress);
            try {
              client.send(msg);
            } catch (RemoteException ignore) {
              ignore.printStackTrace();
            }
          }
        }
      }

      @Override
      public void onEnd(int nid, String filename) {
        if (sPairCache.indexOfKey(nid) >= 0) {
          DownloadTool.Pair pair = sPairCache.get(nid);
          if (pair != null) {
            Model model = pair.model;
            NotificationCompat.Builder builder = pair.currentNotificationBuilder;
            builder.setProgress(100, 100, false)
                .setContentText("100%");
            startForeground(nid, builder.build());
            Bundle data = new Bundle();
            data.putParcelable(Const.EXTRA_MODEL, model);
            // TODO : handle download complete
            Message msg = Message.obtain(null, MSG_COMPLETE, DOWNLOAD_STATUS_SUCCESS, nid);
            msg.setData(data);
            try {
              if (sClients.get(model) != null) {
                sClients.get(model).send(msg);
              }
            } catch (RemoteException e) {
              e.printStackTrace();
            } finally {
              stopForeground(true);
              DownloadTool.clearCache(sPairCache, sClients, nid);
            }
          }
        }
      }

      @Override
      public void onError(int nid, Exception e) {
        if (sPairCache.indexOfKey(nid) >= 0) {
          // do nothing.
        }
      }
    };
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return mMessenger.getBinder();
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }

  @Override
  public void onRebind(Intent intent) {
    super.onRebind(intent);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  private void startDownload(Model model, String cookie) {
    Timber.d("startDownload([" + " title:" + model.title + " url:" + model.url + "])");

    int nId = DownloadTool.buildNotificationID(model);
    DownloadThread thread =
        new DownloadThread(this, model, cookie, nId, mDownloadThreadListener);

    DownloadTool.Pair pair = new DownloadTool.Pair(model, nId);
    pair.pushToCache(sPairCache);

    thread.start();
  }

  public static SparseArray<DownloadTool.Pair> getPairCache() {
    return sPairCache;
  }

  public static Map<Model, Messenger> getClients() {
    return sClients;
  }

  private static class ServiceHandler extends Handler {

    private WeakReference<DownloadService> mWeakService;

    public ServiceHandler(DownloadService service) {
      super();
      mWeakService = new WeakReference<>(service);
    }

    @Override
    public void handleMessage(Message msg) {
      DownloadService downloadService = mWeakService.get();
      if (downloadService == null) {
        return;
      }
      switch (msg.what) {
        case MSG_DOWNLOAD:
          Bundle data = msg.getData();
          data.setClassLoader(Model.class.getClassLoader());
          Model model = data.getParcelable(Const.EXTRA_MODEL);
          if (model == null) {
            return;
          }
          if (DownloadTool.isInDownloadList(model, sClients, msg.replyTo)) {
            // This task is already in downloading list.
            Message msg_to_client = Message.obtain(null, MSG_STATUS, DOWNLOAD_STATUS_DOWNLOADING, 0);
            try {
              msg.replyTo.send(msg_to_client);
            } catch (RemoteException e) {
              e.printStackTrace();
            }
            return;
          } else {
            // Create a new download task.
            if (DeviceConfig.isOnline(downloadService.getApplicationContext())) {
              sClients.put(model, msg.replyTo);
              Message msg_to_client = Message.obtain(null, MSG_START, MSG_START, 0);
              try {
                msg.replyTo.send(msg_to_client);
              } catch (RemoteException e) {
                e.printStackTrace();
              }
              downloadService.startDownload(model, data.getString(Const.KEY_COOKIE));
            } else {
              // No network.
              Message msg_to_client = Message.obtain(null, MSG_STATUS, DOWNLOAD_STATUS_NO_NETWORK, 0);
              try {
                msg.replyTo.send(msg_to_client);
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            }
          }
          break;
        default:
          super.handleMessage(msg);
      }
    }
  }
}
