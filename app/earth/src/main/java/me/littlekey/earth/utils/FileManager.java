package me.littlekey.earth.utils;

import android.os.FileObserver;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.yuanqi.base.utils.CollectionUtils;

import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.event.OnLoadedPageEvent;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.SaveData;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthCrawler;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import timber.log.Timber;

/**
 * Created by littlekey on 16/7/6.
 */
public class FileManager {

  public static final Comparator<File> FILE_COMPARATOR = new Comparator<File>() {
    @Override
    public int compare(File lhs, File rhs) {
      long l = lhs.lastModified();
      long r = rhs.lastModified();
      return l < r ? -1 : (l == r ? 0 : 1);
    }
  };

  public static final int FILE_CHANGE_REMOVE = 0;
  public static final int FILE_CHANGE_ADD = 1;

  private File mRoot;
  private FileObserver mFileObserver;
  private CopyOnWriteArrayList<File> mArtFileList;
  private List<Listener> mListeners;

  public FileManager() {
    mListeners = new ArrayList<>();
  }

  @SuppressWarnings("unused")
  public void addFileChangeListener(Listener listener) {
    if (listener != null) {
      mListeners.add(listener);
    }
  }

  @SuppressWarnings("unused")
  public void removeFileChangeListener(Listener listener) {
    if (listener != null) {
      mListeners.remove(listener);
    }
  }

  public void open(File root) {
    if (dirValid(root)) {
      if (mRoot == null) {
        mRoot = root;
        mArtFileList = new CopyOnWriteArrayList<>(getSortedFiles());
        mFileObserver = new FileObserver(mRoot.getAbsolutePath(), FileObserver.ALL_EVENTS) {
          @Override
          @SuppressWarnings("SynchronizeOnNonFinalField")
          public void onEvent(int event, String path) {
            switch (event & FileObserver.ALL_EVENTS) {
              case CREATE:
              case MOVED_TO:
                synchronized (mArtFileList) {
                  mArtFileList.clear();
                  mArtFileList.addAll(getSortedFiles());
                }
                for (Listener listener: mListeners) {
                  listener.onChange(FILE_CHANGE_ADD);
                }
                break;
              case DELETE:
              case MOVED_FROM:
                mArtFileList.retainAll(getSortedFiles());
                for (Listener listener: mListeners) {
                  listener.onChange(FILE_CHANGE_REMOVE);
                }
                break;
            }
          }
        };
        mFileObserver.startWatching();
      } else if (mRoot != root) {
        close();
        open(root);
      }
      EventBus.getDefault().register(this);
    }
  }

  public void close() {
    if (mRoot != null) {
      EventBus.getDefault().unregister(this);
      mFileObserver.stopWatching();
      mRoot = null;
      mArtFileList = null;
    }
  }

  @SuppressWarnings("unused")
  public void onEventAsync(OnLoadedPageEvent event) {
    Model model;
    if ((model = event.getModel()) != null) {
      File galleryDir = new File(mRoot,
          EarthUtils.formatString(R.string.art_file_name, model.identity, model.token));
      if (galleryDir.exists() && galleryDir.isDirectory()) {
        File saveData = new File(galleryDir, Const.SAVE_DATA);
        FileOutputStream fileOutputStream = null;
        try {
          fileOutputStream = new FileOutputStream(saveData);
          byte[] content = model.art.encode();
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
      }
    }
  }

  private File queryFirstFileAfter(long timestamp) {
    if (!CollectionUtils.isEmpty(mArtFileList)) {
      for (File file: mArtFileList) {
        if (timestamp == 0 || file.lastModified() > timestamp) {
          return file;
        }
      }
    }
    return null;
  }

  public List<Art> artList(long timestamp, int limit, long[] last_timestamp) {
    List<Art> models = new ArrayList<>();
    File tempFile = queryFirstFileAfter(timestamp);
    int index;
    if (tempFile != null && (index = mArtFileList.indexOf(tempFile)) >= 0) {
      for (File galleryDir : mArtFileList.subList(index, Math.min(index + limit, mArtFileList.size()))) {
        if (!dirValid(galleryDir)) {
          continue;
        }
        String filename = galleryDir.getName();
        String[] gidAndToken;
        if (!TextUtils.isEmpty(filename) && (gidAndToken = filename.split("-")).length == 2) {
          FileInputStream fileInputStream = null;
          try {
            File saveDataFile = new File(galleryDir, Const.SAVE_DATA);
            fileInputStream = new FileInputStream(saveDataFile);
            SaveData saveData = SaveData.ADAPTER.decode(fileInputStream);
            CollectionUtils.add(models,
                Art.ADAPTER.decode(EncryptUtils.getInstance().fromSaveData(saveData)));
            if (galleryDir.lastModified() > last_timestamp[0]) {
              last_timestamp[0] = galleryDir.lastModified();
            }
          } catch (IOException e) {
            e.printStackTrace();
            String gid = gidAndToken[0];
            String token = gidAndToken[1];
            RequestFuture<EarthResponse> future = RequestFuture.newFuture();
            EarthRequest request = EarthApplication.getInstance().getRequestManager()
                .newEarthRequest(ApiType.ART_DETAIL, Request.Method.GET, future, future);
            request.appendPath("g");
            request.appendPath(gid);
            request.appendPath(token);
            request.submit();
            try {
              EarthResponse response = future.get(30, TimeUnit.SECONDS);
              Elements artElements = response.document.select("body > div.gm");
              Art art = EarthCrawler.createArtDetailFromElements(artElements, gid, token);
              CollectionUtils.add(models, art);
              EventBus.getDefault().post(new OnLoadedPageEvent(
                  ModelFactory.createModelFromArt(art, Model.Template.DATA)));
              if (galleryDir.lastModified() > last_timestamp[0]) {
                last_timestamp[0] = galleryDir.lastModified();
              }
            } catch (Exception ignore) {
              Timber.e(ignore, EarthUtils.formatString(R.string.parse_error, Const.ART_DETAIL));
            }
          } finally {
            try {
              if (fileInputStream != null) {
                fileInputStream.close();
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
    return models;
  }

  public int size() {
    return mArtFileList == null ? 0 : mArtFileList.size();
  }

  @SuppressWarnings("unused")
  public boolean hasMore(long timestamp) {
    return queryFirstFileAfter(timestamp) != null;
  }

  public File getDownloadDir() {
    return mRoot;
  }

  private List<File> getSortedFiles() {
    List<File> files = Arrays.asList(mRoot.listFiles());
    Collections.sort(files, FILE_COMPARATOR);
    return files;
  }

  private boolean dirValid(File dir) {
    return dir != null && dir.exists() && dir.isDirectory() && dir.canRead() && dir.canWrite();
  }

  public interface Listener {

    void onChange(int mode);
  }
}
