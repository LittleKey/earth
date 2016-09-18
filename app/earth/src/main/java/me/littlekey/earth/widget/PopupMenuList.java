package me.littlekey.earth.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import me.littlekey.base.ReadOnlyList;
import me.littlekey.base.utils.FormatUtils;

import me.littlekey.earth.R;
import me.littlekey.earth.utils.ResourceUtils;

/**
 * Created by littlekey on 16/7/12.
 */
public class PopupMenuList {
  private ReadOnlyList<String> mItemList;
  private PopupWindow mPopupWindow;
  private LayoutInflater mLayoutInflater;

  private OnDismissListener mOnDismissListener;

  public PopupMenuList(Context context) {
    mLayoutInflater = ((FragmentActivity) context).getLayoutInflater();
    View view = mLayoutInflater.inflate(R.layout.popup_menu_list, null);
    ListView listView = (ListView) view.findViewById(R.id.popup_list);
    final PopAdapter popAdapter = new PopAdapter();
    listView.setAdapter(popAdapter);
    listView.setOnItemClickListener(popAdapter);

    mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT);
    mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
      @Override
      public void onDismiss() {
        mOnDismissListener.onDismiss(popAdapter.getChosenPosition());
      }
    });
    mPopupWindow.setFocusable(true);
    mPopupWindow.setOutsideTouchable(true);
  }

  public void setItems(ReadOnlyList<String> items) {
    mItemList = items;
    if (mItemList.size() > 5) {
      mPopupWindow.setHeight(FormatUtils.dipsToPix(150));
    }
  }

  public void showAsDropDown(View parentView) {
    // NOTE : measure Y offset when items size more than 5
    mPopupWindow.showAsDropDown(parentView, 0,
        -parentView.getHeight() - FormatUtils.dipsToPix(45 * mItemList.size()));
    mPopupWindow.update();
  }

  public void dismiss() {
    mPopupWindow.dismiss();
  }

  private class PopAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private int mChosenPosition = -1;

    @Override
    public int getCount() {
      return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public String getItem(int position) {
      return mItemList == null ? null : mItemList.getItem(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        convertView = mLayoutInflater.inflate(R.layout.item_popup_menu, parent, false);
        viewHolder = new ViewHolder(convertView);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      viewHolder.bind(position);
      return convertView;
    }

    private final class ViewHolder {
      TextView textView;
      View bindView;

      public ViewHolder(View view) {
        bindView = view;
        textView = (TextView) view.findViewById(R.id.popup_item_text);
        view.setTag(this);
      }

      public void bind(int position) {
        textView.setText(getItem(position));
        if (position == mChosenPosition) {
          textView.setBackgroundResource(R.color.white_pressed);
          textView.setTextColor(ResourceUtils.getColor(R.color.gray));
        } else {
          textView.setBackgroundResource(R.color.white);
          textView.setTextColor(ResourceUtils.getColor(R.color.black));
        }
      }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      if (mChosenPosition != position) {
        mChosenPosition = position;
        notifyDataSetChanged();
      }
      dismiss();
    }

    public int getChosenPosition() {
      return mChosenPosition;
    }

  }

  public interface OnDismissListener {
    void onDismiss(int chosenPosition);
  }

  public void setOnDismissListener(OnDismissListener onDismissListener) {
    mOnDismissListener = onDismissListener;
  }
}