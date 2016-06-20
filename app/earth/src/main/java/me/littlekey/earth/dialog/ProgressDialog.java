package me.littlekey.earth.dialog;

import android.app.Dialog;
import android.content.Context;

import me.littlekey.earth.R;

/**
 * Created by littlekey on 16/6/20.
 */
public class ProgressDialog extends Dialog {

  public ProgressDialog(Context context) {
    super(context, R.style.AppTheme_DialogNoTitle);
    setContentView(R.layout.dialog_progress);
    setCanceledOnTouchOutside(false);
    setCancelable(false);
  }

}