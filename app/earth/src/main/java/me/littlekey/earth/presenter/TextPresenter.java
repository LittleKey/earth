package me.littlekey.earth.presenter;

import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.utils.EarthUtils;

/**
 * Created by littlekey on 16/6/23.
 */
public class TextPresenter extends EarthPresenter {

  @Override
  public void bind(Model model) {
    Object attrValue = getValueByViewId(id(), model);
    if (attrValue == null) {
      return;
    }
    view().setVisibility(View.VISIBLE);
    if (attrValue instanceof Spannable && view() instanceof TextView) {
      bindSpan((TextView) view(), (Spannable) attrValue);
    } else if (attrValue instanceof CharSequence && view() instanceof TextView) {
      bindText((TextView) view(), (CharSequence) attrValue);
    }
  }

  private void bindText(TextView view, CharSequence attrValue) {
    view.setText(attrValue);
  }

  private void bindSpan(TextView view, Spannable spannable) {
    view.setText(spannable);
    Linkify.addLinks(view, Linkify.ALL);
  }


  private Object getValueByViewId(int id, Model model) {
    switch (id) {
      /** Common **/
      case R.id.content:
        return Html.fromHtml(model.description);
      case R.id.description:
        return model.description;
      case R.id.title:
        return model.title;
      case R.id.nickname:
      case R.id.user_name:
        return model.user.display_name;
      case R.id.avatar:
      case R.id.subtitle:
        return model.subtitle;
      case R.id.date:
        return model.date;
      case R.id.language:
        if (TextUtils.isEmpty(model.language)) {
          return EarthApplication.getInstance().getString(R.string.unknown);
        }
        return model.language;
      case R.id.page_number:
        return EarthUtils.formatString(R.string.page_count, model.count.pages);
      case R.id.size:
        if (TextUtils.isEmpty(model.fileSize)) {
          return EarthApplication.getInstance().getString(R.string.zero_file_size);
        }
        return model.fileSize;
      case R.id.likes:
        return String.valueOf(model.count.likes);
      case R.id.number:
        return String.valueOf(model.count.number);
      case R.id.rating_count:
        return EarthUtils.formatString(R.string.rating_count,
            model.count.rating, model.count.rating_count);
      case R.id.rating:
        return EarthUtils.formatString(R.string.rating_num, model.count.rating);
    }
    return null;
  }

}
