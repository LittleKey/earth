package me.littlekey.earth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.mvp.DataLoadObserver;
import com.yuanqi.mvp.widget.MvpRecyclerView;

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
import me.littlekey.earth.utils.EarthServer;
import timber.log.Timber;

/**
 * Created by littlekey on 16/7/5.
 */
public class DownloadFragment extends BaseFragment {

  private final static Comparator<Model> ADDITION_MODEL_COMPARATOR =
      new Comparator<Model>() {
        @Override
        public int compare(Model lhs, Model rhs) {
          return TextUtils.equals(lhs.identity, rhs.identity)
              && TextUtils.equals(lhs.token, rhs.token) ? 0 : -1;
        }
      };

  private ListAdapter mAdapter;
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
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
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
    mDownloadListCheckAgent.addListener(new DownloadAgent.ListenerAdapter() {

      @Override
      public void onConnect() {
        mDownloadListCheckAgent.checkDownloadList();
      }

      @Override
      public void onList(@Nullable final List<Model> list) {
        if (list != null) {
          for (Model model: list) {
            final int index = Collections.binarySearch(mAdapter.getData(), model, ADDITION_MODEL_COMPARATOR);
            if (index > -1) {
              final Model item = mAdapter.getItem(index);
              DownloadAgent agent = EarthApplication.getInstance().newDownload(model);
              agent.addListener(new DownloadAgent.ListenerAdapter() {
                @Override
                public void onProgress(float progress) {
                  Timber.d("onProgress: " + progress);
                  mAdapter.changeData(index, item.newBuilder()
                      .template(Model.Template.ITEM_DLC_DOWNLOADING)
                      .count(item.count.newBuilder().progress(progress).build())
                      .build());
                }

                @Override
                public void onComplete(boolean succeed) {
                  Timber.d("onComplete: " + succeed);
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
    });
    mDownloadListCheckAgent.connect();
  }

  @Override
  public void onResume() {
    super.onResume();
    mDownloadListCheckAgent.checkDownloadList();
  }

  @Override
  public void onDestroyView() {
    EarthApplication.getInstance().getRequestManager().cancel(this);
    mApiList.unregisterDataLoadObservers();
    if (mServer != null) {
      mServer.stop();
    }
    super.onDestroyView();
  }
}
