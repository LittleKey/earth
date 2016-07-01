package me.littlekey.earth.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

  private SimpleDraweeView mRegCodeImgView;
  private EditText mUserNameView;
  private EditText mDisplayNameView;
  private EditText mEmailView;
  private EditText mPassWordView;
  private EditText mRegCodeView;
  private String mRegCodeID;

  private ProgressDialog mProgressDialog;

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
    mDisplayNameView = (EditText) view.findViewById(R.id.display_name);
    mEmailView = (EditText) view.findViewById(R.id.email);
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
        if (mValidated == 0b1) {
          String username = mUserNameView.getText().toString();
          String displayName = mDisplayNameView.getText().toString();
          String email = mEmailView.getText().toString();
          String password = mPassWordView.getText().toString();
          String regCode = mRegCodeView.getText().toString();
          if ((TextUtils.isEmpty(username) || username.length() < 3)
              || (TextUtils.isEmpty(displayName) || TextUtils.isDigitsOnly(displayName) || displayName.length() < 3)
              || TextUtils.isEmpty(email)
              || (TextUtils.isEmpty(password) || password.length() < 8)
              || (TextUtils.isEmpty(regCode) || regCode.length() != 6)) {
            // TODO : validate register information in TextWatcher
            // TODO : validate username, display_name, email by api
            ToastUtils.toast(R.string.register_info_error);
            return;
          }
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
        }
        break;
    }
  }

  private final Response.Listener<EarthResponse> mStepOneListener = new Response.Listener<EarthResponse>() {
    @Override
    public void onResponse(EarthResponse response) {
      String step2Url = response.document.select("div.page > div > form").attr("action");
      EarthRequest step2Request = EarthApplication.getInstance().getRequestManager()
          .newEarthRequest(step2Url, Request.Method.POST, mStepTwoListener, RegisterFragment.this);
      Map<String, String> params = new HashMap<>();
      params.put(Const.KEY_AGREE_TO_TERMS, Const.ONE);
      step2Request.setParams(params);
      step2Request.setTag(RegisterFragment.this);
      step2Request.setShouldCache(false);
      step2Request.submit();
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
      mValidated |= VALIDATED_REG_CODE_ID;
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
}
