package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import timber.log.Timber;

/**
 * Created by littlekey on 16/6/11.
 */
public class HomeFragment extends BaseFragment {

  public static HomeFragment newInstance() {
    return new HomeFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_home, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    FragmentManager fm = getChildFragmentManager();
    Fragment contentFragment = fm.findFragmentById(R.id.fragment_container);
    if (contentFragment == null) {
      contentFragment = createContentFragment();
      fm.beginTransaction()
          .add(R.id.fragment_container, contentFragment)
          .commit();
    }
    EarthRequest request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(ApiType.LOGIN, Request.Method.POST,
            new Response.Listener<EarthResponse>() {
              @Override
              public void onResponse(EarthResponse response) {
                String cookie = response.headers.get("Set-Cookie");
                if (!TextUtils.isEmpty(cookie)) {
                  String userId = null;
                  String passHash = null;
                  Pattern userIdPattern = Pattern.compile("ipb_member_id=(.*?);");
                  Matcher userIdMatcher = userIdPattern.matcher(cookie);
                  while (userIdMatcher.find()) {
                    if (!TextUtils.equals(userId = userIdMatcher.group(1), "0")) {
                      break;
                    }
                  }
                  Pattern passHashPattern = Pattern.compile("ipb_pass_hash=(.*?);");
                  Matcher passHashMatcher = passHashPattern.matcher(cookie);
                  while (passHashMatcher.find()) {
                    if (!TextUtils.equals(passHash = passHashMatcher.group(1), "0")) {
                      break;
                    }
                  }
                  EarthApplication.getInstance().getAccountManager().login(userId, passHash);
                }
              }
            }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                Timber.e("login error.");
              }
            });
    Map<String, String> params = new HashMap<>();
    params.put("CookieDate", "1");
    params.put("UserName", "LittleKey");
    params.put("PassWord", "JD199401@1");
    request.setParams(params);
    request.setTag(this);
    request.submit();
  }

  @Override
  public void onDestroyView() {
    EarthApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroyView();
  }

  protected Fragment createContentFragment() {
    Bundle bundle = new Bundle();
    bundle.putInt(Const.KEY_API_TYPE, ApiType.HOME_LIST.ordinal());
    return ListFragment.newInstance(bundle);
  }
}
