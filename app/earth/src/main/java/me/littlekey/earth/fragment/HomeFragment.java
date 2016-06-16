package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.utils.Const;

/**
 * Created by littlekey on 16/6/11.
 */
public class HomeFragment extends BaseFragment {

  public static HomeFragment newInstance(Bundle bundle) {
    HomeFragment fragment = new HomeFragment();
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
    bundle.putInt(Const.KEY_API_TYPE, ApiType.HOME_LIST.ordinal());
    bundle.putBundle(Const.KEY_BUNDLE, getArguments());
    return ListFragment.newInstance(bundle);
  }
}
