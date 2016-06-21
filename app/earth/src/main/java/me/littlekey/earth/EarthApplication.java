package me.littlekey.earth;

import android.graphics.Typeface;

import com.facebook.common.internal.ImmutableSet;
import com.yuanqi.base.utils.LogUtils;
import com.yuanqi.mvp.BaseApplication;
import com.yuanqi.network.ApiContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import me.littlekey.earth.account.AccountManager;
import me.littlekey.earth.dialog.CategoryDialog;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.network.EarthRequestManager;
import me.littlekey.earth.utils.Const;
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
      Collections.addAll(mSelectedCategory, CategoryDialog.sCategorys);
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

}

