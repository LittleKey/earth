package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.base.utils.CollectionUtils;

import java.util.List;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.utils.Const;

/**
 * Created by littlekey on 16/6/11.
 */
public class ArtListFragment extends BaseFragment {

  public static ArtListFragment newInstance(Bundle bundle) {
    ArtListFragment fragment = new ArtListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_home, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    FragmentManager fm = getChildFragmentManager();
    Fragment contentFragment = fm.findFragmentById(R.id.fragment_container);
    if (contentFragment == null) {
      contentFragment = createContentFragment();
      fm.beginTransaction()
          .add(R.id.fragment_container, contentFragment)
          .commit();
    }
  }

  @Override
  public void onDestroyView() {
    EarthApplication.getInstance().getRequestManager().cancel(this);
    super.onDestroyView();
  }

  protected Fragment createContentFragment() {
    Bundle bundle = new Bundle();
    Bundle argBundle = getArguments();
    List<String> paths = argBundle.getStringArrayList(Const.KEY_PATH);
    if (!CollectionUtils.isEmpty(paths)) {
      switch (paths.get(0)) {
        case "tag":
          bundle.putInt(Const.KEY_API_TYPE, ApiType.TAG_LIST.ordinal());
          break;
      }
    } else {
      bundle.putInt(Const.KEY_API_TYPE, ApiType.ART_LIST.ordinal());
    }
    bundle.putBundle(Const.KEY_BUNDLE, argBundle);
    return ListFragment.newInstance(bundle);
  }
}
