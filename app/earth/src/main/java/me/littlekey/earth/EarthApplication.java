package me.littlekey.earth;

import android.graphics.Typeface;
import android.text.TextUtils;

import com.facebook.common.internal.ImmutableList;
import com.facebook.common.internal.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.base.utils.LinkedHashTreeSet;
import com.yuanqi.base.utils.LogUtils;
import com.yuanqi.mvp.BaseApplication;
import com.yuanqi.network.ApiContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.littlekey.earth.account.AccountManager;
import me.littlekey.earth.dialog.CategoryDialog;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.network.EarthRequestManager;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.DownloadAgent;
import me.littlekey.earth.utils.DownloadTool;
import me.littlekey.earth.utils.FileManager;
import me.littlekey.earth.utils.PreferenceUtils;

/**
 * Created by littlekey on 16/6/11.
 */
public class EarthApplication extends BaseApplication implements ApiContext {
  private static EarthApplication sBaseApplicationInstance;
  private EarthRequestManager mRequestManager;
//  private UpdateAgent mUpdateAgent;
  private Typeface mIconTypeface;
  private AccountManager mAccountManager;
  private HashSet<Model.Category> mSelectedCategory;
  private Gson mGson;
  private LinkedHashTreeSet<String> mSearchHistories;
  private FileManager mFileManager;

  public EarthApplication() {
    sBaseApplicationInstance = this;
  }

  public static EarthApplication getInstance() {
    return sBaseApplicationInstance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    LogUtils.init(BuildConfig.DEBUG);
    initializeVolley();
//    initializeUpdate();
    initializeAccount();
    initializeIconTypeface();
    initializeSelectedCategory();
    initializeSearchHistory();
    initializeFileManager();
  }

  @Override
  public EarthRequestManager getRequestManager() {
    return mRequestManager;
  }

//  public UpdateAgent getUpdateAgent() {
//    return mUpdateAgent;
//  }

  public AccountManager getAccountManager() {
    return mAccountManager;
  }

  private void initializeVolley() {
    mRequestManager = new EarthRequestManager(this);
  }

//  private void initializeUpdate() {
//    mUpdateAgent = new UpdateAgent(this, this);
//  }

  private void initializeSearchHistory() {
    mGson = new Gson();
    mSearchHistories = new LinkedHashTreeSet<>();
    getSearchHistories();
  }

  private void initializeFileManager() {
    mFileManager = new FileManager();
    mFileManager.open(DownloadTool.getDownloadDir(this, new boolean[1]));
  }

  private void initializeIconTypeface() {
    mIconTypeface = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
  }

  public Typeface getIconTypeface() {
    return mIconTypeface;
  }

  private void initializeAccount() {
    mAccountManager = new AccountManager();
  }

  private void initializeSelectedCategory() {
    mSelectedCategory = new HashSet<>();
    Set<String> lastCategory = PreferenceUtils.getStringSet(Const.LAST_SELECTED, Const.LAST_CATEGORY, null);
    if (lastCategory != null) {
      for (String item: lastCategory) {
        mSelectedCategory.add(Model.Category.from(item));
      }
    } else {
      // NOTE : default select all category
      Collections.addAll(mSelectedCategory, CategoryDialog.sCategories);
    }
  }

  public Set<Model.Category> getSelectedCategory() {
    return ImmutableSet.copyOf(mSelectedCategory);
  }

  public void addSelectCategory(Model.Category category) {
    mSelectedCategory.add(category);
    Set<String> categoryNameSet = new HashSet<>();
    for (Model.Category c: mSelectedCategory) {
      categoryNameSet.add(c.getName());
    }
    PreferenceUtils.setStringSet(Const.LAST_SELECTED, Const.LAST_CATEGORY, categoryNameSet);
  }

  public void removeSelectCategory(Model.Category category) {
    mSelectedCategory.remove(category);
    Set<String> categoryNameSet = new HashSet<>();
    for (Model.Category c: mSelectedCategory) {
      categoryNameSet.add(c.getName());
    }
    PreferenceUtils.setStringSet(Const.LAST_SELECTED, Const.LAST_CATEGORY, categoryNameSet);
  }

  public void addSearchHistory(String content) {
    if (TextUtils.isEmpty(content)) {
      return;
    }
    mSearchHistories.add(0, content);
    while (mSearchHistories.size() > Const.SEARCH_HISTORY_RECORD_COUNT) {
      mSearchHistories.remove(Const.SEARCH_HISTORY_RECORD_COUNT);
    }
    JsonArray jsonArray = new JsonArray();
    for (String item: mSearchHistories) {
      jsonArray.add(item);
    }
    PreferenceUtils.setString(Const.LAST_HISTORY, Const.LAST_SEARCH, jsonArray.toString());
  }

  public List<String> getSearchHistories() {
    if (CollectionUtils.isEmpty(mSearchHistories)) {
      String searchHistoriesString = PreferenceUtils.getString(Const.LAST_HISTORY,
          Const.LAST_SEARCH, Const.EMPTY_STRING);
      JsonArray jsonArray = mGson.fromJson(searchHistoriesString, JsonArray.class);
      if (!TextUtils.isEmpty(searchHistoriesString)) {
        for (JsonElement item : jsonArray) {
          mSearchHistories.add(item.getAsString());
        }
      }
    }
    return ImmutableList.copyOf(mSearchHistories);
  }

  public DownloadAgent newDownload(Model model) {
    return new DownloadAgent(this, model,
        getRequestManager().convertCookies(getRequestManager().buildCookie()));
  }

  public FileManager getFileManager() {
    return mFileManager;
  }
}

