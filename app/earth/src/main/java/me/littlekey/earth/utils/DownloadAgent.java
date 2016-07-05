package me.littlekey.earth.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.littlekey.earth.model.Model;
import me.littlekey.earth.service.DownloadService;

/**
 * Created by littlekey on 16/7/4.
 */
public class DownloadAgent {

  private final Messenger mClientMessenger = new Messenger(new ClientHandler(this));
  private final Context mContext;
  private Messenger mServiceMessenger;
  private Model mModel;
  private String mCookie;
  private boolean mIsDownloading = false;
  private List<Listener> mListeners;

  private final ServiceConnection mConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mServiceMessenger = new Messenger(service);
      try {
        Message msg = Message.obtain(null, DownloadService.MSG_DOWNLOAD);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.EXTRA_MODEL, mModel);
        bundle.putString(Const.KEY_COOKIE, mCookie);
        msg.setData(bundle);
        msg.replyTo = mClientMessenger;
        mServiceMessenger.send(msg);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mServiceMessenger = null;
    }
  };

  public DownloadAgent(Context context, Model model, String cookie) {
    mContext = context;
    mModel = model;
    mCookie = cookie;
    mListeners = new ArrayList<>();
  }

  public boolean isDownloading() {
    return mIsDownloading;
  }

  public void addListener(Listener listener) {
    if (listener != null) {
      mListeners.add(listener);
    }
  }

  public void removeListener(Listener listener) {
    if (listener != null) {
      mListeners.remove(listener);
    }
  }

  public void start() {
    Intent intent = new Intent(mContext, DownloadService.class);
    mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
  }

  private void onStart() {
    for (Listener listener: mListeners) {
      listener.onStart();
    }
  }

  private void onComplete(boolean succeed) {
    for (Listener listener: mListeners) {
      listener.onComplete(succeed);
    }
  }

  private void onProgress(float progress) {
    for (Listener listener: mListeners) {
      listener.onProgress(progress);
    }
  }

  private void onBadNetwork() {
    for (Listener listener: mListeners) {
      listener.onBadNetwork();
    }
  }

  private static class ClientHandler extends Handler {

    private WeakReference<DownloadAgent> mWeakAgent;

    public ClientHandler(DownloadAgent agent) {
      super();
      mWeakAgent = new WeakReference<>(agent);
    }

    @Override
    public void handleMessage(Message msg) {
      DownloadAgent agent = mWeakAgent.get();
      if (agent == null) {
        return;
      }
      try {
        switch (msg.what) {
          case DownloadService.MSG_START:
            agent.mIsDownloading = true;
            agent.onStart();
            break;
          case DownloadService.MSG_COMPLETE:
            agent.mIsDownloading = false;
            agent.onComplete(msg.arg1 == DownloadService.DOWNLOAD_STATUS_SUCCESS);
            break;
          case DownloadService.MSG_STATUS:
            if (msg.arg1 == DownloadService.DOWNLOAD_STATUS_DOWNLOADING) {
              agent.mIsDownloading = true;
              if (msg.obj != null && msg.obj instanceof Float) {
                agent.onProgress((float) msg.obj);
              }
            }
            if (msg.arg1 == DownloadService.DOWNLOAD_STATUS_NO_NETWORK) {
              agent.onBadNetwork();
            }
            break;
          default:
            super.handleMessage(msg);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public interface Listener {

    void onStart();

    void onComplete(boolean succeed);

    void onProgress(float progress);

    void onBadNetwork();
  }
}
