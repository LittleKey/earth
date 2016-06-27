package me.littlekey.earth.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.MultiAutoCompleteTextView;

import com.squareup.wire.Wire;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.base.utils.FormatUtils;

import java.util.ArrayList;
import java.util.Iterator;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;

/**
 * Created by littlekey on 16/6/27.
 */
public class SearchCompleteView extends AppCompatMultiAutoCompleteTextView
    implements View.OnTouchListener {

  public SearchCompleteView(Context context) {
    this(context, null);
  }

  public SearchCompleteView(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.autoCompleteTextViewStyle);
  }

  public SearchCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    setAdapter(new SearchHistoryAdapter(context));
    setDropDownVerticalOffset(FormatUtils.dipsToPix(10));
    setOnTouchListener(this);
  }

  @Override
  public int getDropDownHeight() {
    return ViewGroup.LayoutParams.WRAP_CONTENT;
  }

  @Override
  public boolean onKeyPreIme(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing()) {
      InputMethodManager inputManager = (InputMethodManager) getContext()
          .getSystemService(Context.INPUT_METHOD_SERVICE);
      if(inputManager.hideSoftInputFromWindow(findFocus().getWindowToken(),
          InputMethodManager.HIDE_NOT_ALWAYS)) {
        dismissDropDown();
        return true;
      }
    }

    return super.onKeyPreIme(keyCode, event);
  }

  @Override
  public int getThreshold() {
    return 0;
  }

  @Override
  protected void performFiltering(CharSequence text, int start, int end, int keyCode) {
    super.performFiltering(text, start, end, keyCode);
    showDropDown();
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_UP:
        performFiltering(getText(), 0);
        break;
    }
    return false;
  }

  private static final class SearchHistoryAdapter extends ArrayAdapter<String> {

    private Filter mFilter;

    public SearchHistoryAdapter(Context context) {
      super(context, R.layout.item_search_complete);
    }

    @Override
    public int getPosition(String item) {
      return super.getPosition(item);
    }

    @Override
    public int getCount() {
      return super.getCount();
    }

    @Override
    public String getItem(int position) {
      return super.getItem(position);
    }

    @Override
    public Filter getFilter() {
      if (mFilter == null) {
        mFilter = new Filter() {

          @Override
          @SuppressWarnings("unchecked")
          protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            results.values = new ArrayList<>();
            CollectionUtils.addAll((ArrayList<String>) results.values,
                EarthApplication.getInstance().getSearchHistories());
            Iterator<String> iterator = ((ArrayList<String>) results.values).iterator();
            while (iterator.hasNext()) {
              if (!iterator.next().toLowerCase().startsWith(Wire.get(constraint, "").toString().toLowerCase())
                  && !TextUtils.isEmpty(constraint)) {
                iterator.remove();
              }
            }
            results.count = ((ArrayList<String>) results.values).size();
            return results;
          }

          @Override
          protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<String> filteredList = (ArrayList<String>) results.values;
            clear();
            if(filteredList != null && results.count > 0) {
              addAll(filteredList);
            }
            notifyDataSetChanged();
          }
        };
      }
      return mFilter;
    }
  }
}
