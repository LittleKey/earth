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
  }

  public boolean isDownloading() {
    return mIsDownloading;
  }

  public void start() {
    Intent intent = new Intent(mContext, DownloadService.class);
    mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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
            break;
          case DownloadService.MSG_COMPLETE:
            agent.mIsDownloading = false;
            break;
          case DownloadService.MSG_STATUS:
            if (msg.arg1 == DownloadService.DOWNLOAD_STATUS_DOWNLOADING) {
              agent.mIsDownloading = true;
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
}
