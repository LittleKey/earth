package me.littlekey.earth.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.activity.HomeActivity;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.ToastUtils;
import me.littlekey.earth.widget.StatefulButton;

/**
 * Created by littlekey on 16/6/13.
 */
public class LoginFragment extends BaseFragment
    implements
      View.OnClickListener,
      Response.Listener<EarthResponse>,
      Response.ErrorListener {

  private EditText mAccount;
  private EditText mPassword;
  private StatefulButton mBtnLogin;
  private Dialog mProDialog;

  private boolean mValidateAccount = false;
  private boolean mValidatePassword = false;

  TextWatcher mAccountWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      mValidateAccount = !TextUtils.isEmpty(s);
    }


    @Override
    public void afterTextChanged(Editable s) {
      validate();
    }
  };
  TextWatcher mPassWordWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      mValidatePassword = !TextUtils.isEmpty(s);
    }


    @Override
    public void afterTextChanged(Editable s) {
      validate();
    }
  };

  public static LoginFragment newInstance() {
    return new LoginFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_login, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mProDialog = new ProgressDialog(getActivity());
    mBtnLogin = (StatefulButton) view.findViewById(R.id.btn_login);
    mAccount = (EditText) view.findViewById(R.id.account_name);
    mPassword = (EditText) view.findViewById(R.id.account_password);

    mAccount.addTextChangedListener(mAccountWatcher);
    mPassword.addTextChangedListener(mPassWordWatcher);
    mBtnLogin.setOnClickListener(this);
  }

  @Override
  public void onDestroyView() {
    mProDialog.dismiss();
    EarthApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroyView();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_login:
        mProDialog.show();
        login(mAccount.getText().toString(), mPassword.getText().toString());
        break;
    }
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    ToastUtils.toast(R.string.login_error);
    mProDialog.dismiss();
  }

  @Override
  public void onResponse(EarthResponse response) {
    mProDialog.dismiss();
    String cookie = response.headers.get(Const.KEY_SET_COOKIE);
    if (!TextUtils.isEmpty(cookie)) {
      String userId = null;
      String passHash = null;
      Pattern userIdPattern = Pattern.compile("ipb_member_id=(.*?);");
      Matcher userIdMatcher = userIdPattern.matcher(cookie);
      while (userIdMatcher.find()) {
        if (!TextUtils.equals(userId = userIdMatcher.group(1),Const.ZERO)) {
          break;
        }
      }
      Pattern passHashPattern = Pattern.compile("ipb_pass_hash=(.*?);");
      Matcher passHashMatcher = passHashPattern.matcher(cookie);
      while (passHashMatcher.find()) {
        if (!TextUtils.equals(passHash = passHashMatcher.group(1), Const.ZERO)) {
          break;
        }
      }
      if (TextUtils.isEmpty(userId) || TextUtils.equals(userId, Const.ZERO)
          || TextUtils.isEmpty(userId) || TextUtils.equals(userId, Const.ZERO)) {
        ToastUtils.toast(R.string.login_error);
        return;
      }
      EarthApplication.getInstance().getAccountManager().login(userId, passHash);
      Intent intent = new Intent(getActivity(), HomeActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
      getActivity().finish();
    }
  }

  private void validate() {
    if (mValidateAccount && mValidatePassword) {
      mBtnLogin.setEnabled(true);
      mBtnLogin.setState(StatefulButton.STATE_DONE);
    } else {
      mBtnLogin.setEnabled(false);
      mBtnLogin.setState(StatefulButton.STATE_CANCELED);
    }
  }

  private void login(String username, String password) {
    EarthRequest request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(ApiType.LOGIN, Request.Method.POST, this, this);
    Map<String, String> params = new HashMap<>();
    params.put(Const.KEY_COOKIE_DATE, Const.ONE);
    params.put(Const.KEY_USER_NAME, username);
    params.put(Const.KEY_PASSWORD, password);
    request.setParams(params);
    request.setTag(this);
    request.submit();
  }
}
