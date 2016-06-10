package me.littlekey.earth;

import android.graphics.Typeface;

import com.yuanqi.base.utils.LogUtils;
import com.yuanqi.mvp.BaseApplication;
import com.yuanqi.network.ApiContext;

import me.littlekey.earth.account.AccountManager;
import me.littlekey.earth.network.EarthRequestManager;

/**
 * Created by littlekey on 16/6/11.
 */
public class EarthApplication extends BaseApplication implements ApiContext {
  private static EarthApplication sBaseApplicationInstance;
  private EarthRequestManager mRequestManager;
//  private UpdateAgent mUpdateAgent;
  private Typeface mIconTypeface;
  private AccountManager mAccountManager;

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

}

