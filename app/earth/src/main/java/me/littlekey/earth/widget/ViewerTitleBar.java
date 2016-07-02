package me.littlekey.earth.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import me.littlekey.earth.R;
import me.littlekey.earth.activity.BaseActivity;

/**
 * Created by littlekey on 16/7/1.
 */
public class ViewerTitleBar extends RelativeLayout implements View.OnClickListener {

  public ViewerTitleBar(Context context) {
    this(context, null);
  }

  public ViewerTitleBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ViewerTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    findViewById(R.id.back).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.back:
        ((BaseActivity) getContext()).finish();
        break;
    }
  }

  public void show() {
    setVisibility(VISIBLE);
  }

  public void hide() {
    setVisibility(GONE);
  }
}
