package me.littlekey.earth.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import me.littlekey.earth.R;

/**
 * Created by littlekey on 16/7/1.
 */
public class ViewerControlBar extends RelativeLayout {

  private SeekBar mSeekBar;
  private TextView mCurrentPageView;
  private TextView mTotalPageView;

  public ViewerControlBar(Context context) {
    this(context, null);
  }

  public ViewerControlBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ViewerControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
    mCurrentPageView = (TextView) findViewById(R.id.current_page);
    mTotalPageView = (TextView) findViewById(R.id.total_page);
  }

  public void setSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener) {
    mSeekBar.setOnSeekBarChangeListener(listener);
  }

  public void setTotalPage(int totalPage) {
    mSeekBar.setMax(totalPage - 1);
    mTotalPageView.setText(String.valueOf(totalPage));
  }

  public void setCurrentPage(int currentPage) {
    mSeekBar.setProgress(currentPage);
    mCurrentPageView.setText(String.valueOf(currentPage + 1));
  }

  public void show() {
    setVisibility(VISIBLE);
  }

  public void hide() {
    setVisibility(GONE);
  }
}
