package me.littlekey.earth.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanqi.base.ReadOnlyList;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.base.utils.FormatUtils;
import com.yuanqi.mvp.widget.MvpRecyclerView;
import com.yuanqi.network.NameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlekey.earth.R;
import me.littlekey.earth.adapter.OfflineListAdapter;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;

/**
 * Created by littlekey on 16/7/12.
 */
public class AdvancedSearchFragment extends BaseFragment {

  public static final Model.Category[] sCategories = {
      Model.Category.DOUJINSHI, Model.Category.MANGA,
      Model.Category.ARTIST_CG, Model.Category.GAME_CG,
      Model.Category.WESTERN, Model.Category.NON__H,
      Model.Category.IMAGE_SET, Model.Category.COSPLAY,
      Model.Category.ASIAN_PORN, Model.Category.MISC,
  };

  public static final NameValuePair[] sAdvancedOptions = {
      new NameValuePair(Const.NAME, "搜索画廊"), new NameValuePair(Const.TAGS, "搜索画廊标签"),
      new NameValuePair(Const.DEST, "搜索画廊描述"), new NameValuePair(Const.TORR, "搜索种子文件名"),
      new NameValuePair(Const.TO, "仅显示有种子的画廊"), new NameValuePair(Const.DT_ONE, "搜索低愿力标签"),
      new NameValuePair(Const.DT_TWO, "搜索差评标签"), new NameValuePair(Const.H, "显示被删除的画廊"),
      new NameValuePair(Const.R, "最低评分:"),
  };

  public static final float[] sRatings = {
      2, 3, 4, 5
  };

  public static final ReadOnlyList<String> sRatingsReadOnlyList = new ReadOnlyList<String>() {
    @Override
    public String getItem(int position) {
      return EarthUtils.formatString(R.string.rating_num, sRatings[position]);
    }

    @Override
    public int size() {
      return sRatings.length;
    }
  };

  private OfflineListAdapter mAdapter;

  public static AdvancedSearchFragment newInstance() {
    return new AdvancedSearchFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_advanced_search, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    RecyclerView recyclerView = (MvpRecyclerView) view.findViewById(R.id.recycler);
    recyclerView.setItemAnimator(null);
    mAdapter = new OfflineListAdapter();
    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        switch (position) {
          case 0:
          case 11:
            return 2;
          default:
            return 1;
        }
      }
    });
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int _10dp = FormatUtils.dipsToPix(10);
        int _5dp = _10dp / 2;
        if (position == 0) {
          outRect.set(_10dp, _10dp, _10dp, _10dp);
        }
        --position;
        if (position >= 0 && position < sCategories.length) {
          if (position % 2 == 0) {
            // left
            outRect.set(_10dp, _5dp, _5dp, _5dp);
          } else {
            // right
            outRect.set(_5dp, _5dp, _10dp, _5dp);
          }
          if (position <= 1) {
            outRect.top = _10dp;
          }
          if (position >= sCategories.length - 2) {
            outRect.bottom = _10dp;
          }
        }
        position -= sCategories.length;
        if (position == 0) {
          outRect.set(_10dp, _10dp, _10dp, _10dp);
        }
        --position;
        if (position >= 0 && position < sAdvancedOptions.length + 1) {
          if (position % 2 == 0) {
            // left
            outRect.set(_10dp, _5dp, _5dp, _5dp);
          } else {
            // right
            outRect.set(_5dp, _5dp, _10dp, _5dp);
          }
          if (position <= 1) {
            outRect.top = _10dp;
          }
          if (position >= sCategories.length - 2) {
            outRect.bottom = _10dp;
          }
        }
        position -= sAdvancedOptions.length;
      }
    });
    recyclerView.setAdapter(mAdapter);
    List<Model> models = new ArrayList<>();
    CollectionUtils.add(models, new Model.Builder()
        .type(Model.Type.TEXT)
        .template(Model.Template.TITLE)
        .title(getString(R.string.select_category))
        .build());
    for (Model.Category category: sCategories) {
      CollectionUtils.add(models,
          ModelFactory.createModelFromCategory(category, Model.Template.CATEGORY));
    }
    CollectionUtils.add(models, new Model.Builder()
        .type(Model.Type.TEXT)
        .template(Model.Template.TITLE)
        .title(getString(R.string.advanced_option))
        .build());
    for (NameValuePair pair: sAdvancedOptions) {
      Flag flag = new Flag.Builder()
          .is_selected(pair == sAdvancedOptions[0] || pair == sAdvancedOptions[1])
          .build();
      Map<Integer, Action> actions = new HashMap<>();
      actions.put(Const.ACTION_MAIN, new Action.Builder()
          .type(Action.Type.SELECT_ADV_OPT)
          .build());
      CollectionUtils.add(models, new Model.Builder()
          .template(Model.Template.ADVANCED_SEARCH)
          .title(pair.getName())
          .description(pair.getValue())
          .flag(flag)
          .actions(actions)
          .build());
    }
    Map<Integer, Action> actions = new HashMap<>();
    actions.put(Const.ACTION_MAIN, new Action.Builder()
        .type(Action.Type.POPUP_RATING)
        .build());
    Count count = new Count.Builder().rating(2f).build();
    CollectionUtils.add(models, new Model.Builder()
        .template(Model.Template.RATING_SELECT)
        .count(count)
        .actions(actions)
        .build());
    mAdapter.setData(models);
  }

  public List<Model> getSearchQuery() {
    return Collections.unmodifiableList(mAdapter.getData());
  }
}
