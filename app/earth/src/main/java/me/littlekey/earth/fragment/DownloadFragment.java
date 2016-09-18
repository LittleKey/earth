package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.littlekey.mvp.DataLoadObserver;
import me.littlekey.mvp.widget.MvpRecyclerView;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.adapter.ListAdapter;
import me.littlekey.earth.model.EarthApiList;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.data.FilesDataGenerator;
import me.littlekey.earth.model.proto.GetDownloadFileResponse;
import me.littlekey.earth.utils.DownloadAgent;
import me.littlekey.earth.network.EarthServer;

/**
 * Created by littlekey on 16/7/5.
 */
public class DownloadFragment extends BaseFragment implements DownloadAgent.Listener {

  private final static Comparator<Model> ADDITION_MODEL_COMPARATOR =
      new Comparator<Model>() {
        @Override
        public int compare(Model lhs, Model rhs) {
          return TextUtils.equals(lhs.identity, rhs.identity)
              && TextUtils.equals(lhs.token, rhs.token) ? 0 : -1;
        }
      };

  private ListAdapter mAdapter;
  private LinearLayoutManager mLayoutManager;
  private DownloadAgent mDownloadListCheckAgent;
  private EarthApiList<GetDownloadFileResponse> mApiList;
  private EarthServer mServer;

  public static DownloadFragment newInstance() {
    return new DownloadFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    try {
      mServer = new EarthServer();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return inflater.inflate(R.layout.fragment_download, container, false);
  }

  @Override
  public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
    MvpRecyclerView recyclerView = (MvpRecyclerView) view.findViewById(R.id.recycler);
    mAdapter = new ListAdapter();
    mLayoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setAdapter(mAdapter);
    recyclerView.setItemAnimator(null);
    mApiList = new EarthApiList<>(new FilesDataGenerator(mServer.getListeningPort()));
    mApiList.registerDataLoadObserver(new DataLoadObserver<Model>() {
      @Override
      public void onLoadStart(Op op) {

      }

      @Override
      public void onLoadSuccess(Op op, OpData<Model> opData) {
        mDownloadListCheckAgent.checkDownloadList();
      }

      @Override
      public void onLoadError(Op op, Exception e) {

      }
    });
    mApiList.registerDataLoadObserver(mAdapter);
    mAdapter.setList(mApiList);
    mApiList.refresh();
    mDownloadListCheckAgent = EarthApplication.getInstance().newDownload(null);
    mDownloadListCheckAgent.addListener(this);
    mDownloadListCheckAgent.connect();
  }

  @Override
  public void onResume() {
    super.onResume();
    mAdapter.tryPreLoad(mLayoutManager.findLastVisibleItemPosition(), mAdapter.getItemCount());
  }

  @Override
  public void onDestroyView() {
    EarthApplication.getInstance().getRequestManager().cancel(this);
    mApiList.unregisterDataLoadObservers();
    mDownloadListCheckAgent.removeListener(this);
    if (mServer != null) {
      mServer.stop();
    }
    super.onDestroyView();
  }

  @Override
  public void onConnect() {
    mDownloadListCheckAgent.checkDownloadList();
  }

  @Override
  public void onDisconnect() {

  }

  @Override
  public void onComplete(boolean succeed) {

  }

  @Override
  public void onProgress(float progress) {

  }

  @Override
  public void onBadNetwork() {

  }

  @Override
  public void onList(@Nullable List<Model> list) {
    if (list == null) {
      return;
    }
    for (Model model: list) {
      final int index = Collections.binarySearch(mAdapter.getData(), model, ADDITION_MODEL_COMPARATOR);
      if (index > -1) {
        final Model item = mAdapter.getItem(index);
        DownloadAgent agent = EarthApplication.getInstance().newDownload(model);
        agent.addListener(new DownloadAgent.ListenerAdapter() {
          @Override
          public void onProgress(float progress) {
            mAdapter.changeData(index, item.newBuilder()
                .template(Model.Template.ITEM_DLC_DOWNLOADING)
                .count(item.count.newBuilder().progress(progress).build())
                .build());
          }

          @Override
          public void onComplete(boolean succeed) {
            mAdapter.changeData(index, item.newBuilder()
                .template(Model.Template.ITEM_DLC)
                .build());
          }
        });
        agent.connect();
      }
    }
  }
}
