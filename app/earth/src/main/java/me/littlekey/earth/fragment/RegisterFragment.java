package me.littlekey.earth.fragment;

import android.content.Intent;
import android.net.Uri;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.HashMap;
import java.util.Map;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.activity.LoginActivity;
import me.littlekey.earth.dialog.ProgressDialog;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.ToastUtils;

/**
 * Created by littlekey on 16/7/1.
 */
public class RegisterFragment extends BaseFragment
    implements Response.ErrorListener, View.OnClickListener {

  private final static int VALIDATED_NONE =         0;
  private final static int VALIDATED_REG_CODE_ID =  1;
  private final static int VALIDATED_USER_NAME =    1 << 1;
  private final static int VALIDATED_DISPLAY_NAME = 1 << 2;
  private final static int VALIDATE_EMAIL_ADDRESS = 1 << 3;

  private ProgressDialog mProgressDialog;
  private SimpleDraweeView mRegCodeImgView;
  private EditText mUserNameView;
  private EditText mDisplayNameView;
  private EditText mEmailAddressView;
  private EditText mPassWordView;
  private EditText mRegCodeView;

  private String mRegCodeID;
  private String mRegSession;

  private int mValidated = VALIDATED_NONE;

  public static RegisterFragment newInstance() {
    return new RegisterFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_register, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    EarthApplication.getInstance().getAccountManager().logout();
    mProgressDialog = new ProgressDialog(getActivity());
    mRegCodeImgView = (SimpleDraweeView) view.findViewById(R.id.reg_code_img);
    mUserNameView = (EditText) view.findViewById(R.id.username);
    mUserNameView.addTextChangedListener(mUserNameWatch);
    mDisplayNameView = (EditText) view.findViewById(R.id.display_name);
    mDisplayNameView.addTextChangedListener(mDisplayNameWatch);
    mEmailAddressView = (EditText) view.findViewById(R.id.email);
    mEmailAddressView.addTextChangedListener(mEmailAddressWatch);
    mPassWordView = (EditText) view.findViewById(R.id.password);
    mRegCodeView = (EditText) view.findViewById(R.id.reg_code);
    EarthRequest step1Request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(ApiType.REGISTER, Request.Method.GET, mStepOneListener, RegisterFragment.this);
    step1Request.setTag(this);
    step1Request.submit();
    view.findViewById(R.id.btn_register).setOnClickListener(this);
  }

  @Override
  public void onDestroyView() {
    mProgressDialog.dismiss();
    EarthApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroyView();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_register:
        if (mValidated == (VALIDATED_REG_CODE_ID
            | VALIDATE_EMAIL_ADDRESS
            | VALIDATED_DISPLAY_NAME
            | VALIDATED_USER_NAME)) {
          String password = mPassWordView.getText().toString();
          String regCode = mRegCodeView.getText().toString();
          if ((TextUtils.isEmpty(password) || password.length() < Const.MIN_PASSWORD_LENGTH)
              || (TextUtils.isEmpty(regCode) || regCode.length() != Const.REGISTER_CODE_LENGTH)) {
            ToastUtils.toast(R.string.register_info_error);
            return;
          }
          String username = mUserNameView.getText().toString();
          String displayName = mDisplayNameView.getText().toString();
          String email = mEmailAddressView.getText().toString();
          EarthRequest step3Request = EarthApplication.getInstance().getRequestManager()
              .newEarthRequest(Const.API_REGISTER, Request.Method.POST, mStepThreeListener, RegisterFragment.this);
          Map<String, String> params = new HashMap<>();
          params.put(Const.KEY_TEMPORARY_HTTPS, Const.ONE);
          params.put(Const.KEY_ACT, Const.REG);
          params.put(Const.KEY_TERMSREAD, Const.ONE);
          params.put(Const.KEY_AGREE_TO_TERMS, Const.ONE);
          params.put(Const.KEY_CODE, Const.ZERO_TWO);
          params.put(Const.KEY_COPPA_USER, Const.ZERO);
          params.put(Const.KEY_USER_NAME, username);
          params.put(Const.KEY_MEMBERS_DISPLAY_NAME, displayName);
          params.put(Const.KEY_PASSWORD, password);
          params.put(Const.KEY_PASSWORD_CHECK, password);
          params.put(Const.KEY_EMAIL_ADDRESS, email);
          params.put(Const.KEY_EMAIL_ADDRESS_TWO, email);
          params.put(Const.KEY_TIME_OFFSET, Const.EIGHT);
          params.put(Const.KEY_REG_ID, mRegCodeID);
          params.put(Const.KEY_REG_CODE, regCode);
          step3Request.setParams(params);
          step3Request.setShouldCache(false);
          step3Request.setTag(this);
          step3Request.submit();
          mProgressDialog.show();
        } else {
          ToastUtils.toast(R.string.register_info_error);
        }
        break;
    }
  }

  private final TextWatcher mUserNameWatch = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      String username = s.toString();
      removeFlag(VALIDATED_USER_NAME);
      if (!TextUtils.isEmpty(username) && username.length() >= Const.MIN_USERNAME_LENGTH) {
        callCheckApi(Const.CHECK_USER_NAME, username);
      }
    }
  };

  private final TextWatcher mDisplayNameWatch = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      String displayName = s.toString();
      removeFlag(VALIDATED_DISPLAY_NAME);
      if (!TextUtils.isEmpty(displayName) && !TextUtils.isDigitsOnly(displayName)
          && displayName.length() >= Const.MIN_DISPLAY_NAME_LENGTH) {
        callCheckApi(Const.CHECK_DISPLAY_NAME, displayName);
      }
    }
  };

  private final TextWatcher mEmailAddressWatch = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      String emailAddress = s.toString();
      removeFlag(VALIDATE_EMAIL_ADDRESS);
      if (!TextUtils.isEmpty(emailAddress)) {
        callCheckApi(Const.CHECK_EMAIL_ADDRESS, emailAddress);
      }
    }
  };

  private final Response.Listener<EarthResponse> mStepOneListener = new Response.Listener<EarthResponse>() {
    @Override
    public void onResponse(EarthResponse response) {
      String step2Url = response.document.select("div.page > div > form").attr("action");
      mRegSession = Uri.parse(step2Url).getQueryParameter(Const.KEY_S);
      EarthRequest step2Request = EarthApplication.getInstance().getRequestManager()
          .newEarthRequest(step2Url, Request.Method.POST, mStepTwoListener, RegisterFragment.this);
      Map<String, String> params = new HashMap<>();
      params.put(Const.KEY_AGREE_TO_TERMS, Const.ONE);
      step2Request.setParams(params);
      step2Request.setTag(RegisterFragment.this);
      step2Request.setShouldCache(false);
      step2Request.submit();
      removeFlag(VALIDATED_REG_CODE_ID);
    }
  };

  private final Response.Listener<EarthResponse> mStepTwoListener = new Response.Listener<EarthResponse>() {
    @Override
    public void onResponse(EarthResponse response) {
      String url = response.document
          .select("div.tablepad > table > tbody > tr:nth-child(2) > td > fieldset.row3 > table > tbody > tr > td > img")
          .attr("src");
      Uri uri = Uri.parse(url);
      ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
          .setResizeOptions(new ResizeOptions(300, 80));
      PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder()
          .setImageRequest(imageRequestBuilder.build())
          .setTapToRetryEnabled(true)
          .setOldController(mRegCodeImgView.getController());
      mRegCodeImgView.setController(controllerBuilder.build());
      mRegCodeID = uri.getQueryParameter(Const.KEY_RC);
      addFlag(VALIDATED_REG_CODE_ID);
    }
  };

  private final Response.Listener<EarthResponse> mStepThreeListener = new Response.Listener<EarthResponse>() {
    @Override
    public void onResponse(EarthResponse response) {
      mProgressDialog.dismiss();
      // TODO : check success
      Intent intent = new Intent(getActivity(), LoginActivity.class);
      startActivity(intent);
      getActivity().finish();
    }
  };

  @Override
  public void onErrorResponse(VolleyError error) {
    mProgressDialog.dismiss();
    ToastUtils.toast(R.string.register_error);
  }

  private void callCheckApi(final String checkOp, String name) {
    if (TextUtils.isEmpty(mRegSession) || TextUtils.isEmpty(name)) {
      return;
    }
    long nowMicroSecond = System.nanoTime() / 1000;
    EarthRequest request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(ApiType.CHECK, Request.Method.GET,
            new Response.Listener<EarthResponse>() {
              @Override
              public void onResponse(EarthResponse response) {
                String result = response.document.body().text();
                if (!TextUtils.isEmpty(result) && TextUtils.equals(result, Const.NOT_FOUND)) {
                  int flag;
                  switch (checkOp) {
                    case Const.CHECK_USER_NAME:
                      flag = VALIDATED_USER_NAME;
                      break;
                    case Const.CHECK_DISPLAY_NAME:
                      flag = VALIDATED_DISPLAY_NAME;
                      break;
                    case Const.CHECK_EMAIL_ADDRESS:
                      flag = VALIDATE_EMAIL_ADDRESS;
                      break;
                    default:
                      return;
                  }
                  addFlag(flag);
                }
              }
            }, RegisterFragment.this);
    Map<String, String> queries = new HashMap<>();
    queries.put(Const.KEY_S, mRegSession);
    queries.put(Const.KEY_ACT, Const.XML_OUT);
    queries.put(Const.KEY_DO, checkOp);
    queries.put(Const.KEY_NAME, name);
    queries.put(Const.KEY__, String.valueOf(nowMicroSecond));
    request.setQuery(queries);
    request.setShouldCache(false);
    request.setTag(RegisterFragment.this);
    request.submit();
  }

  private synchronized void addFlag(int flag) {
    mValidated |= flag;
  }

  private synchronized void removeFlag(int flag) {
    mValidated &= ~flag;
  }
}
