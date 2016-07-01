package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.base.utils.CollectionUtils;

import org.apache.commons.collections4.list.FixedSizeList;

import java.util.Arrays;
import java.util.List;

import me.littlekey.earth.R;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.FutureSupplier;
import me.littlekey.earth.utils.PictureTokenFuture;
import me.littlekey.earth.utils.TokenSupplier;

/**
 * Created by littlekey on 16/6/28.
 */
public class ViewerFragment extends BaseFragment
    implements TokenSupplier, FutureSupplier<PictureTokenFuture> {

  private String mGalleryToken;
  private String mGid;
  private FixedSizeList<String> mTokenList;
  private FixedSizeList<PictureTokenFuture> mFutureList;

  public static ViewerFragment newInstance(Bundle bundle) {
    ViewerFragment fragment = new ViewerFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_viewer, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    ViewPager viewPager = (ViewPager) view.findViewById(R.id.picture_viewpager);
    FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

      @Override
      public Fragment getItem(int position) {
        return createFragment(position);
      }

      @Override
      public int getCount() {
        return mTokenList == null ? 0 : mTokenList.maxSize();
      }
    };
    mGalleryToken = getArguments().getString(Const.EXTRA_TOKEN);
    mTokenList = FixedSizeList.fixedSizeList(
        Arrays.asList(new String[getArguments().getInt(Const.EXTRA_PAGES, 0)]));
    mFutureList = FixedSizeList.fixedSizeList(
        Arrays.asList(new PictureTokenFuture[
            getArguments().getInt(Const.EXTRA_PAGES, 0) / Const.IMAGE_ITEM_COUNT_PER_PAGE + 1]));
    viewPager.setAdapter(pagerAdapter);
    List<String> paths = getArguments().getStringArrayList(Const.KEY_API_PATH);
    if (paths != null && paths.size() == 3) {
      String[] subPaths = paths.get(2).split("-");
      mGid = subPaths[0];
      insertAll(mGid, getArguments().getStringArrayList(Const.KEY_TOKEN_LIST), 0);
      int position = Integer.valueOf(subPaths[1]) - 1;
      mTokenList.set(position, paths.get(1));
      viewPager.setCurrentItem(position);
    }
    viewPager.setOffscreenPageLimit(5);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  @SuppressWarnings("SynchronizeOnNonFinalField")
  public PictureTokenFuture get(@Nullable String token, String gid, int position) {
    int page = position / Const.IMAGE_ITEM_COUNT_PER_PAGE;
    if (mFutureList != null && mGid != null && TextUtils.equals(mGid, gid)
        && mFutureList.maxSize() > page) {
      synchronized (mFutureList) {
        PictureTokenFuture future = mFutureList.get(page);
        if (future == null) {
          future = new PictureTokenFuture(this, token == null ? mGalleryToken : token, gid, page);
          mFutureList.set(page, future);
        }
        return future;
      }
    }
    return null;
  }

  @Override
  @SuppressWarnings("SynchronizeOnNonFinalField")
  public @Nullable String get(String gid, int position) {
    if (mTokenList != null && mGid != null && TextUtils.equals(mGid, gid)
        && mTokenList.maxSize()  > position) {
      synchronized (mTokenList) {
        return mTokenList.get(position);
      }
    }
    return null;
  }

  @Override
  @SuppressWarnings("SynchronizeOnNonFinalField")
  public void insertAll(String gid, List<String> tokens, int page) {
    if (mTokenList != null && mGid != null && TextUtils.equals(mGid, gid)
        && !CollectionUtils.isEmpty(tokens)) {
      synchronized (mTokenList) {
        // TODO : optimize
        for (int i = 0; i < tokens.size(); ++i) {
          mTokenList.set(page * Const.IMAGE_ITEM_COUNT_PER_PAGE + i, tokens.get(i));
        }
      }
    }
  }

  protected Fragment createFragment(int position) {
    return PictureFragment.newInstance(mGid, position);
  }
}