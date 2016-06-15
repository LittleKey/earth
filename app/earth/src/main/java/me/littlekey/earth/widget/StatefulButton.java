package me.littlekey.earth.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import me.littlekey.earth.R;

/**
 * Created by littlekey on 16/6/13.
 */
public class StatefulButton extends FrameLayout {
  public static final int STATE_GONE = -1;
  public static final int STATE_DONE = 1;
  public static final int STATE_CANCELED = 2;
  private Map<Integer, Integer> mStateMap = new HashMap<>();
  private int mLayout;
  private TextView mTextView;

  public StatefulButton(Context context) {
    this(context, null);
  }

  public StatefulButton(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public StatefulButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.StatefulButton);
    mStateMap.put(STATE_DONE, a.getResourceId(R.styleable.StatefulButton_stateDone, 0));
    mStateMap.put(STATE_CANCELED, a.getResourceId(R.styleable.StatefulButton_stateCanceled, 0));
    mLayout = a.getResourceId(R.styleable.StatefulButton_layoutRes, R.layout.stateful_button);
    a.recycle();
    init();
  }

  protected void init() {
    LayoutInflater.from(getContext()).inflate(mLayout, this, true);
    mTextView = (TextView) findViewById(R.id.stateful_btn_label);
  }

  public void setState(int state) {
    if (state == STATE_GONE) {
      setVisibility(GONE);
      return;
    } else if (getVisibility() == GONE) {
      setVisibility(VISIBLE);
    }
    setState(getContext(), mStateMap.get(state));
  }

  private void setState(Context context, int resId) {
    TypedArray appearance =
        context.obtainStyledAttributes(resId,
            R.styleable.StatefulButton);

    int n = appearance.getIndexCount();
    for (int i = 0; i < n; i++) {
      int attr = appearance.getIndex(i);
      switch (attr) {
        case R.styleable.StatefulButton_android_textColor:
          setTextColor(appearance.getColorStateList(attr));
          break;
        case R.styleable.StatefulButton_android_textSize:
          setTextSize(appearance.getDimensionPixelSize(attr, 0));
          break;
        case R.styleable.StatefulButton_android_text:
          setText(appearance.getString(attr));
          break;
        case R.styleable.StatefulButton_android_gravity:
          setGravity(appearance.getInt(attr, -1));
          break;
        case R.styleable.StatefulButton_android_singleLine:
          setSingleLine(appearance.getBoolean(attr, true));
          break;
        case R.styleable.StatefulButton_android_background:
          setBackgroundDrawable(appearance.getDrawable(attr));
          break;
        case R.styleable.StatefulButton_android_drawable:
          mTextView.setBackgroundDrawable(appearance.getDrawable(attr));
          break;
        case R.styleable.StatefulButton_android_alpha:
          setAlpha(appearance.getFloat(attr, 1));
          break;
      }
    }
    appearance.recycle();
  }

  protected void setSingleLine(boolean singleLine) {
    mTextView.setSingleLine(singleLine);
  }

  protected void setGravity(int gravity) {
    mTextView.setGravity(gravity);
  }

  protected void setTextSize(int dimensionPixelSize) {
    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimensionPixelSize);
  }


  protected void setTextColor(ColorStateList textColor) {
    mTextView.setTextColor(textColor);
  }

  protected void setText(String text) {
    mTextView.setText(text);
  }
}

