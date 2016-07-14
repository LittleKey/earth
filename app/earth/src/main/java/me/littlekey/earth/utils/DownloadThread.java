package me.littlekey.earth.utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;

import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.base.utils.DeviceConfig;

import org.apache.commons.collections4.list.FixedSizeList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.proto.Picture;
import me.littlekey.earth.model.proto.SaveData;
import me.littlekey.earth.network.EarthCrawler;
import me.littlekey.earth.service.DownloadService;

/**
 * Created by littlekey on 16/7/5.
 */
public class DownloadThread extends Thread {

  private final static int MAX_REPEAT_COUNT = 3;

  private static final long WAIT_FOR_REPEAT = 3 * 1000;

  private WeakReference<DownloadService> mWeakService;
  private Context mContext;
  private Model mModel;
  private String mCookie;
  private int mNotificationId;
  private Listener mListener;

  private boolean isSdCard;
  private File dir;
  private final FixedSizeList<File> mFileList;
  private final FixedSizeList<String> mImageTokens;
  private final FixedSizeList<String> mImageSrcUrls;

  private ExecutorService mExecutor;
  private AtomicInteger mProgress;

  public DownloadThread(DownloadService service, Model model, String cookie, int nid, Listener listener) {
    super();
    mWeakService = new WeakReference<>(service);
    mContext = service.getApplicationContext();
    mModel = model;
    mCookie = cookie;
    mNotificationId = nid;
    mListener = listener;
    boolean[] SdCard = new boolean[1];
    dir = DownloadTool.getDownloadDir(mContext, SdCard);
    isSdCard = SdCard[0];
    // TODO clean update download folder.
    // long cache_size = isSdCard
    // ? EXTERNAL_CACHE_SIZE
    // : INTERNAL_CACHE_SIZE;
    // UpdateUtils.checkDir(dir, cache_size, OVERDUE_TIME);
    String airDir = EarthUtils.formatString(R.string.art_file_name,
        model.addition.identity, model.addition.token);
    dir = new File(dir, airDir);
    dir.mkdir();
    int pages = model.addition.count.pages;
    mFileList = FixedSizeList.fixedSizeList(Arrays.asList(new File[pages]));
    for (File file: dir.listFiles()) {
      String filename = file.getName();
      String filename_without_ext;
      if (filename.endsWith(".jpg") && TextUtils.isDigitsOnly(
          filename_without_ext = filename.substring(0, filename.length() - 4))) {
        mFileList.set(Integer.valueOf(filename_without_ext), file);
      }
    }
    mImageTokens = FixedSizeList.fixedSizeList(Arrays.asList(new String[pages]));
    mImageSrcUrls = FixedSizeList.fixedSizeList(Arrays.asList(new String[pages]));
    mExecutor = Executors.newFixedThreadPool(Math.max(2, Math.min(pages / 5, 10)));
    mProgress = new AtomicInteger(0);
  }

  public void run() {
    // add notification
    try {
      if (mListener != null) {
        mListener.onStart(mNotificationId);
      }
      boolean SUCCEED = saveArt();
      try {
        Messenger client;
        if (SUCCEED) {
          if (mListener != null) {
            mListener.onEnd(mNotificationId, dir.getName());
          }
          if ((client = DownloadService.getClients().get(mModel)) != null) {
            client.send(Message.obtain(null, DownloadService.MSG_COMPLETE, DownloadService.DOWNLOAD_STATUS_SUCCESS, 0));
          }
        } else {
          DownloadTool.clearCache((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE),
              DownloadService.getPairCache(), DownloadService.getClients(), mNotificationId);
          if ((client = DownloadService.getClients().get(mModel)) != null) {
            client.send(Message.obtain(null, DownloadService.MSG_COMPLETE, DownloadService.DOWNLOAD_STATUS_FAIL, 0));
          }
        }
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      if (DownloadService.getClients().size() <= 0) {
        DownloadService service = mWeakService.get();
        if (service != null) {
          service.stopSelf();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean saveArt() {
    boolean SUCCEED = true;
    List<Future<Boolean>> futures = new ArrayList<>();
    for (int i = 0; i < mModel.addition.count.pages; ++i) {
      futures.add(callback(i));
    }
    File saveData = new File(dir, Const.SAVE_DATA);
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(saveData);
      byte[] content = mModel.addition.art.encode();
      SaveData.ADAPTER.encode(fileOutputStream, EncryptUtils.getInstance().toSaveData(content));
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (fileOutputStream != null) {
          fileOutputStream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    for (Future<Boolean> future: futures) {
      try {
        SUCCEED &= future.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
        SUCCEED = false;
      }
    }
    return SUCCEED;
  }

  private Future<Boolean> callback(final int position) {
    return mExecutor.submit(new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        boolean rlt = savePicture(position, 0);
        if (mListener != null) {
          int total = mModel.addition.count.pages;
          mListener.onProgress(mNotificationId,
              Math.min(100, mProgress.incrementAndGet() * 100 / total));
        }
        return rlt;
      }
    });
  }

  private void turnPage(int position) {
    int page = position / Const.IMAGE_ITEM_COUNT_PER_PAGE;
    synchronized (mImageTokens) {
      if (mImageTokens.get(position) != null) {
        return;
      }
      Uri uri = Uri.parse(Const.API_ROOT).buildUpon()
          .appendEncodedPath("g")
          .appendEncodedPath(mModel.addition.identity)
          .appendEncodedPath(mModel.addition.token)
          .appendQueryParameter(Const.KEY_P, String.valueOf(page))
          .build();
      try {
        URL u = new URL(uri.toString());
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("GET");
        conn.addRequestProperty(Const.KEY_COOKIE, mCookie);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(10000);
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        Document document = Jsoup.parse(inputStream, "utf8", uri.toString());
        int i = 0;
        for (Element element : document.select("#gdt > div > a")) {
          List<String> paths = Uri.parse(element.attr("href")).getPathSegments();
          if (!CollectionUtils.isEmpty(paths)) {
            mImageTokens.set(page * Const.IMAGE_ITEM_COUNT_PER_PAGE + i++, paths.get(1));
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
        if (mListener != null) {
          mListener.onError(mNotificationId, e);
        }
      }
    }
  }

  private String getPictureSrc(String token, int position) {
    String url = null;
    Uri uri = Uri.parse(Const.API_ROOT).buildUpon()
        .appendEncodedPath("s")
        .appendEncodedPath(token)
        .appendEncodedPath(EarthUtils.formatString("%s-%d", mModel.addition.identity, position + 1))
        .build();
    Document document = null;
    try {
      URL u = new URL(uri.toString());
      HttpURLConnection conn = (HttpURLConnection) u.openConnection();
      conn.setRequestMethod("GET");
      conn.addRequestProperty(Const.KEY_COOKIE, mCookie);
      conn.setConnectTimeout(5000);
      conn.setReadTimeout(10000);
      conn.connect();
      InputStream inputStream = conn.getInputStream();
      document = Jsoup.parse(inputStream, "utf8", uri.toString());
    } catch (IOException e) {
      e.printStackTrace();
      mListener.onError(mNotificationId, e);
    }
    if (document != null) {
      try {
        Elements pictureElements = document.select("#i1");
        Picture picture = EarthCrawler.createPictureFromElements(pictureElements);
        mImageSrcUrls.set(position, url = picture.image.src);
      } catch (Exception e) {
        e.printStackTrace();
        if (mListener != null) {
          mListener.onError(mNotificationId, e);
        }
      }
    }
    return url;
  }

  @SuppressWarnings("deprecation")
  @SuppressLint({"WorldReadableFiles", "WorldWriteableFiles"})
  private boolean savePicture(int position, int repeatCount) {
    File file = mFileList.get(position);
    if (file == null) {
      file = new File(dir, EarthUtils.formatString("%d.jpg.tmp", position));
      mFileList.set(position, file);
    }
    if (!file.getName().endsWith(".tmp")) {
      return true;
    }
    String filename = file.getName();
    InputStream inputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(file, true);
      if (!isSdCard) {
        if (DownloadTool.setFilePermissionsFromMode(file.getAbsolutePath(), Context.MODE_WORLD_READABLE
            | Context.MODE_WORLD_WRITEABLE)) {
          fileOutputStream.close();
          fileOutputStream = mContext.openFileOutput(filename, Context.MODE_WORLD_READABLE
              | Context.MODE_WORLD_WRITEABLE
              | Context.MODE_APPEND);
          mFileList.set(position, file = mContext.getFileStreamPath(filename));
        }
      }
      String url = mImageSrcUrls.get(position);
      if (url == null) {
        String token = mImageTokens.get(position);
        if (token == null) {
          turnPage(position);
          token = mImageTokens.get(position);
          if (token == null) {
            return false;
          }
        }
        mImageSrcUrls.set(position, url = getPictureSrc(token, position));
      }
      URL u = new URL(url);
      HttpURLConnection conn = initConnection(u, file);
      conn.connect();
      inputStream = conn.getInputStream();
      @SuppressWarnings("unused")
      long accLen = file.length();
      long totalLen = conn.getContentLength();

      byte[] buffer = new byte[4096];
      int cycle = 0, len;
      int STEP = 50;
      boolean SUCCEED = true;

      while ((len = inputStream.read(buffer)) > 0) {
        fileOutputStream.write(buffer, 0, len);
        accLen += len;
        if ((cycle++) % STEP == 0) {
          if (!DeviceConfig.isOnline(mContext)) {
            SUCCEED = false;
            break;
          }
//          float progress = (float) accLen / (float) totalLen;
//          if (progress > 1) {
//            progress = 0.99f;
//          }
//          if (mListener != null) {
//            int total = mModel.addition.count.pages;
//            // NOTE : only valid on single thread
//            mListener.onProgress(mNotificationId, (position + progress) * 100 / total);
//          }
        }
      }
      inputStream.close();
      fileOutputStream.close();
      if (SUCCEED) {
        File newFile = new File(file.getParent(), file.getName().replace(".tmp", ""));
        file.renameTo(newFile);
        if (newFile.length() != totalLen) {
          throw new IOException(EarthUtils.formatString("[File] %s not integrity", file.toString()));
        }
      }
      return SUCCEED;
    } catch (IOException e) {
      if (++repeatCount > MAX_REPEAT_COUNT) {
        mListener.onError(mNotificationId, e);
      } else {
        return repeatConnect(position, repeatCount);
      }
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          if (fileOutputStream != null) {
            fileOutputStream.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }

  private boolean repeatConnect(int position, int repeatCount) {
    try {
      Thread.sleep(WAIT_FOR_REPEAT);
      return savePicture(position, repeatCount);
    } catch (InterruptedException e) {
      if (mListener != null) {
        mListener.onError(mNotificationId, e);
      }
    }
    return false;
  }

  private HttpURLConnection initConnection(URL u, File file) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
    conn.setRequestMethod("GET");
    conn.addRequestProperty("Connection", "keep-alive");
    conn.setConnectTimeout(5000);
    conn.setReadTimeout(10000);
    if (file.exists() && file.length() > 0) {
      conn.setRequestProperty("Range", "bytes=" + file.length() + "-");
    }
    return conn;
  }

  public interface Listener {

    void onStart(int nid);

    void onProgress(int nid, float progress);

    void onEnd(int nid, String filename);

    void onError(int nid, Exception e);
  }
}
