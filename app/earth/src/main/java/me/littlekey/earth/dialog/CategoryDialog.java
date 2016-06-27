package me.littlekey.earth.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.yuanqi.base.utils.FormatUtils;
import com.yuanqi.mvp.widget.MvpRecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.littlekey.earth.R;
import me.littlekey.earth.adapter.OfflineListAdapter;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;

/**
 * Created by littlekey on 16/6/22.
 */
public class CategoryDialog extends DialogFragment {

  public static final Model.Category[] sCategorys = {
      Model.Category.DOUJINSHI, Model.Category.MANGA,
      Model.Category.ARTIST_CG, Model.Category.GAME_CG,
      Model.Category.WESTERN, Model.Category.NON__H,
      Model.Category.IMAGE_SET, Model.Category.COSPLAY,
      Model.Category.ASIAN_PORN, Model.Category.MISC,
  };

  public static CategoryDialog newInstance() {
    CategoryDialog dialog = new CategoryDialog();
    Bundle bundle = new Bundle();
    dialog.setArguments(bundle);
    return dialog;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_category, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    RecyclerView recyclerView = (MvpRecyclerView) view.findViewById(R.id.recycler);
    OfflineListAdapter adapter = new OfflineListAdapter();
    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        switch (position) {
          case 0:
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
        int count = parent.getAdapter().getItemCount();
        int _10dp = FormatUtils.dipsToPix(10);
        int _5dp = _10dp / 2;
        if (position == 0) {
          outRect.set(_10dp, _10dp, _10dp, _10dp);
        }
        --position;
        --count;
        if (position >= 0 && position < count) {
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
          if (position >= count - 2) {
            outRect.bottom = _10dp;
          }
        }
      }
    });
    recyclerView.setAdapter(adapter);
    List<Model> models = new ArrayList<>();
    models.add(new Model.Builder()
        .type(Model.Type.TEXT)
        .template(Model.Template.TITLE)
        .title(getString(R.string.select_category))
        .build());
    for (Model.Category category: sCategorys) {
      models.add(ModelFactory.createModelFromCategory(category, Model.Template.CATEGORY));
    }
    adapter.setData(models);
  }

  @Override
  public void onStart() {
    super.onStart();
    WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
    lp.dimAmount = 0f;
    lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    getDialog().getWindow().setAttributes(lp);
  }

  public void show(Context context) {
    if (context instanceof FragmentActivity) {
      show(((FragmentActivity) context).getSupportFragmentManager(), CategoryDialog.class.getSimpleName());
    }
  }
}
