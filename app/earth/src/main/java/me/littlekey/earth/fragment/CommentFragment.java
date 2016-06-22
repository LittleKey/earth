package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.base.utils.LinkedHashTreeSet;
import com.yuanqi.mvp.widget.MvpRecyclerView;

import java.util.List;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.R;
import me.littlekey.earth.adapter.OfflineListAdapter;
import me.littlekey.earth.event.OnLoadedCommentsEvent;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.ResourceUtils;
import me.littlekey.earth.widget.DividerItemDecoration;

/**
 *  Created by littlekey on 16/6/23.
 */
public class CommentFragment extends BaseFragment {

  private OfflineListAdapter mAdapter;
  private List<Model> mComments;
  private String mGalleryIdentity;

  public static CommentFragment newInstance(Bundle bundle) {
    CommentFragment fragment = new CommentFragment();
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
    MvpRecyclerView recyclerView = (MvpRecyclerView) view.findViewById(R.id.recycler);
    mGalleryIdentity = getArguments().getString(Const.EXTRA_IDENTITY);
    mAdapter = new OfflineListAdapter();
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(null);
    recyclerView.addItemDecoration(new DividerItemDecoration(
        ResourceUtils.getDrawable(R.drawable.recycler_view_divider_line), 0));
    recyclerView.setAdapter(mAdapter);
    mComments = new LinkedHashTreeSet<>();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroyView() {
    EventBus.getDefault().unregister(this);
    super.onDestroyView();
  }

  protected int getLayout() {
    return R.layout.fragment_comments;
  }

  @SuppressWarnings("unused")
  public void onEventMainThread(OnLoadedCommentsEvent event) {
    if (TextUtils.equals(event.getIdentity(), mGalleryIdentity)) {
      if (!CollectionUtils.isEmpty(event.getComments())) {
        mComments.addAll(event.getComments());
        mAdapter.setData(mComments);
      }
    }
  }
}
