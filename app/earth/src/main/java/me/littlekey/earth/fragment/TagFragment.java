package me.littlekey.earth.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.mvp.widget.MvpRecyclerView;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.R;
import me.littlekey.earth.adapter.OfflineListAdapter;
import me.littlekey.earth.event.OnClickTagItemEvent;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.NavigationManager;

/**
 * Created by littlekey on 16/6/18.
 */
public class TagFragment extends BaseFragment {

  private Model mModel;

  public static TagFragment newInstance(Bundle bundle) {
    TagFragment fragment = new TagFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(getLayout(), container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    final MvpRecyclerView recyclerView = (MvpRecyclerView) view.findViewById(R.id.recycler);
    OfflineListAdapter adapter = new OfflineListAdapter();
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager =
        new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    mModel = getArguments().getParcelable(Const.EXTRA_MODEL);
    if (mModel != null) {
      adapter.setData(mModel.getSubModels());
    }
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroyView() {
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }

  protected @LayoutRes int getLayout() {
    return R.layout.fragment_tag;
  }

  public void onEventMainThread(OnClickTagItemEvent event) {
    int index;
    if ((index = mModel.getSubModels().indexOf(event.getTag())) != -1) {
      String url = buildUrl(mModel.getSubModels().get(index));
      Bundle bundle = new Bundle();
      bundle.putString(Const.EXTRA_URL, url);
      NavigationManager.navigationTo(getActivity(),
          NavigationManager.buildUri(Uri.parse(url).getEncodedPath()), bundle);
    }
  }

  private String buildUrl(Model model) {
    return new Uri.Builder()
        .scheme("http")
        .appendEncodedPath("/exhentai.org")
        .appendEncodedPath(NavigationManager.TAG)
        .appendPath(String.format("%s:%s",
            mModel.getTitle(), TextUtils.join("+", model.getTitle().split(Const.STRING_SPACE))))
        .build()
        .toString();
  }
}
