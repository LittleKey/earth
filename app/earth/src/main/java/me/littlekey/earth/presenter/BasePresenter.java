package me.littlekey.earth.presenter;


import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.yuanqi.base.utils.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.widget.CustomRatingBar;

/**
 * Created by nengxiangzhou on 15/5/8.
 */
public class BasePresenter extends EarthPresenter {
  @Override
  public void bind(Model model) {
    Object attrValue = getValueByViewId(id(), model);
    if (attrValue == null) {
      bindNull(view());
      return;
    }
    view().setVisibility(View.VISIBLE);
    if (attrValue instanceof Integer && view() instanceof ProgressBar) {
      ((ProgressBar) view()).setProgress((Integer) attrValue);
    } else if (attrValue instanceof Float &&
        (view() instanceof RatingBar || view() instanceof CustomRatingBar)) {
      ((RatingBar) view()).setRating((Float) attrValue);
    }
  }

  private void bindNull(View view) {
    // view.setVisibility(View.GONE);
  }

  private int getTotalLeaf(Model model) {
    if (!CollectionUtils.isEmpty(model.getSubModels())) {
      int num = 0;
      for (Model child: model.getSubModels()) {
        num += getTotalLeaf(child);
      }
      return num;
    }
    return 1;
  }

  private Object getValueByViewId(int id, Model model) {
    switch (id) {
      /** Common **/
      case R.id.rating:
        return model.getCount().rating;
    }
    return null;
  }

  private String buildTimeVague(Long time) {
    if (time == null) {
      return Const.EMPTY_STRING;
    }
    Calendar targetTime = Calendar.getInstance();
    targetTime.setTimeInMillis(time);
    Calendar sign = Calendar.getInstance();
    sign.set(Calendar.HOUR_OF_DAY, 0);
    sign.set(Calendar.MINUTE, 0);
    sign.set(Calendar.SECOND, 0);
    sign.set(Calendar.MILLISECOND, 0);
    if (targetTime.after(sign)) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(
          EarthApplication.getInstance().getString(R.string.date_time), Locale.CHINESE);
      return dateFormat.format(targetTime.getTime());
    }
    sign.add(Calendar.DATE, -1);
    if (targetTime.after(sign)) {
      return EarthApplication.getInstance().getString(R.string.yesterday);
    }
    sign.add(Calendar.DATE, -6);
    if (targetTime.after(sign)) {
      SimpleDateFormat weekFormat = new SimpleDateFormat(
          EarthApplication.getInstance().getString(R.string.date_week), Locale.CHINESE);
      return weekFormat.format(targetTime.getTime());
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        EarthApplication.getInstance().getString(R.string.date_ymd), Locale.CHINESE);
    return dateFormat.format(targetTime.getTime());
  }

  private String buildTime(Long time) {
    if (time == null) {
      return Const.EMPTY_STRING;
    }
    SimpleDateFormat format = new SimpleDateFormat(
        EarthApplication.getInstance().getString(R.string.date_time_24), Locale.getDefault());
    return format.format(new Date(time * 1000));
  }

  private String buildDate(Long time) {
    if (time == null) {
      return Const.EMPTY_STRING;
    }
    SimpleDateFormat format = new SimpleDateFormat(
        EarthApplication.getInstance().getString(R.string.date_format), Locale.getDefault());
    return format.format(new Date(time * 1000));
  }
}
