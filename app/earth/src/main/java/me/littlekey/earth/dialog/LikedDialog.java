package me.littlekey.earth.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.network.NameValuePair;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.R;
import me.littlekey.earth.event.OnLikedEvent;
import me.littlekey.earth.fragment.ListFragment;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.utils.Const;

/**
 * Created by littlekey on 16/6/20.
 */
public class LikedDialog extends DialogFragment {

  public static LikedDialog newInstance(String gid, String token) {
    LikedDialog dialog = new LikedDialog();
    Bundle bundle = new Bundle();
    bundle.putInt(Const.KEY_API_TYPE, ApiType.LIKED.ordinal());
    ArrayList<NameValuePair> pairs = new ArrayList<>();
    pairs.add(new NameValuePair(Const.KEY_GID, gid));
    pairs.add(new NameValuePair(Const.KEY_T, token));
    pairs.add(new NameValuePair(Const.KEY_ACT, Const.ADD_FAV));
    bundle.putSerializable(Const.KEY_API_QUERY, pairs);
    dialog.setArguments(bundle);
    return dialog;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_liked, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    FragmentManager fm = getChildFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);
    if (fragment == null) {
      fm.beginTransaction()
          .add(R.id.fragment_container, createFragment())
          .commit();
    }
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroyView() {
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }

  public void onEventMainThread(OnLikedEvent event) {
    dismiss();
  }

  public void show(Context context) {
    if (context instanceof FragmentActivity) {
      show(((FragmentActivity) context).getSupportFragmentManager(), LikedDialog.class.getSimpleName());
    }
  }

  protected Fragment createFragment() {
    return ListFragment.newInstance(getArguments());
  }
}
