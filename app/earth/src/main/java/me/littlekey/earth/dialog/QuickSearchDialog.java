package me.littlekey.earth.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import me.littlekey.earth.R;
import me.littlekey.earth.data.QuickSearchUtils;

/**
 * Created by littlekey on 16/7/13.
 */
public class QuickSearchDialog extends Dialog implements TextView.OnEditorActionListener {

  private String mUrl;
  private EditText mEditName;

  public QuickSearchDialog(Context context, String url) {
    super(context);
    setContentView(R.layout.dialog_quick_search);
    mUrl = url;
    mEditName = (EditText) findViewById(R.id.edit_name);
    mEditName.setOnEditorActionListener(this);
    findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        submit();
      }
    });
  }

  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if (actionId == EditorInfo.IME_ACTION_DONE) {
      submit();
      return true;
    }
    return false;
  }

  private void submit() {
    String name = mEditName.getText().toString();
    if (!TextUtils.isEmpty(name)) {
      QuickSearchUtils.append(getContext().getContentResolver(), name, mUrl);
      dismiss();
    }
  }
}
