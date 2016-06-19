package me.littlekey.earth.presenter;

import android.app.ActivityOptions;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.squareup.wire.Wire;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.mvp.widget.MvpRecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.event.OnClickTagItemEvent;
import me.littlekey.earth.event.OnSelectEvent;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.NavigationManager;


/**
 * Created by nengxiangzhou on 16/1/13.
 */
public class ActionPresenter extends EarthPresenter {

  @Override
  public void bind(final Model model) {
    final Action action = getValueByViewId(id(), model);
    if (action == null) {
      return;
    }
    view().setOnClickListener(new View.OnClickListener() {
      @Override
      @SuppressWarnings("unchecked")
      public void onClick(View v) {
        switch (action.type) {
          case JUMP:
            if (null != action.clazz) {
              NavigationManager.navigationTo(view().getContext(), action.clazz, action.bundle);
            } else if (null != action.uri) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                  && !TextUtils.isEmpty(action.transitionName)) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    (FragmentActivity) view().getContext(), view().findViewById(R.id.cover), action.transitionName);
                NavigationManager.navigationTo(view().getContext(), action.uri, action.bundle, options);
              } else {
                NavigationManager.navigationTo(view().getContext(), action.uri, action.bundle);
              }
            } else if (null != action.url) {
              NavigationManager.navigationTo(view().getContext(), action.url);
            }
            break;
          case LOGOUT:
            logout();
            break;
          case RUNNABLE:
            if (action.runnable != null) {
              action.runnable.run();
            }
            break;
          case SELECT_MEMBER:
            select(model, false);
            break;
          case SELECT_STYLE:
          case SELECT_STATURE:
            check(model);
            break;
          case EVENT:
            EventBus.getDefault().post(new OnClickTagItemEvent(model));
            break;
        }
      }
    });
  }


  private void logout() {
    EarthApplication.getInstance().getAccountManager().logout();
  }

  @SuppressWarnings("unchecked")
  private void select(Model model, boolean updateParent) {
    boolean willSelect = model.getFlag() == null || !Wire.get(model.getFlag().is_selected, false);
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    Flag flag = Wire.get(model.getFlag(), new Flag.Builder().build()).newBuilder()
        .is_selected(willSelect).build();
    Model newModel = new Model.Builder(model).flag(flag).build();
    if (updateParent) {
      updateParentSubModels(adapter, model, newModel);
    }
    adapter.changeData(group().holder.getAdapterPosition(), newModel);
    EventBus.getDefault().post(new OnSelectEvent(willSelect, model));
  }

  @SuppressWarnings("unchecked")
  private void check(Model model) {
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    Flag flag =
        Wire.get(model.getFlag(), new Flag.Builder().build()).newBuilder().is_selected(true)
            .build();
    Model newModel = new Model.Builder(model).flag(flag).build();
    for (int i = 0; i < adapter.getData().size(); i++) {
      Object item = adapter.getItem(i);
      if (item instanceof Model && (flag = ((Model) item).getFlag()) != null
          && Wire.get(flag.is_selected, false) && !item.equals(model)) {
        flag = flag.newBuilder().is_selected(false).build();
        adapter.changeData(i, new Model.Builder((Model) item).flag(flag).build());
        break;
      }
    }
    adapter.changeData(group().holder.getAdapterPosition(), newModel);
    EventBus.getDefault().post(new OnSelectEvent(true, model));
  }

  @SuppressWarnings("unchecked")
  private void updateParentSubModels(MvpRecyclerView.Adapter adapter, Model model, Model newModel) {
    int parentPosition = findParentModelPosition(model);
    if (parentPosition != -1) {
      Model parent = (Model) adapter.getItem(parentPosition);
      List<Model> subModels = new ArrayList<>(parent.getSubModels());
      subModels.set(subModels.indexOf(model), newModel);
      int selectedNum = 0;
      for (Model sub : subModels) {
        Flag flag = sub.getFlag();
        if (!CollectionUtils.isEmpty(sub.getSubModels())) {
          selectedNum += sub.getCount() != null ? Wire.get(sub.getCount().selected_num, 0) : 0;
        } else if (flag != null && Wire.get(flag.is_selected, false)) {
          ++selectedNum;
        }
      }
      Count count = new Count.Builder().selected_num(selectedNum).build();
      Model newParent = new Model.Builder(parent).subModels(subModels).count(count).build();
      updateParentSubModels(adapter, parent, newParent);
      adapter.changeData(parentPosition, newParent);
    }
  }

  @SuppressWarnings("unchecked")
  private void expand(Model model, boolean shouldExpand) {
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    // int position = group().holder.getAdapterPosition();
    int position = adapter.indexOf(model);
    if (shouldExpand != (model.getFlag() != null && Wire.get(model.getFlag().is_selected, false))) {
      Model newModel = new Model.Builder(model).flag(
          new Flag.Builder().is_selected(shouldExpand).build()).build();
      updateParentSubModels(adapter, model, newModel);
      adapter.changeData(position, newModel);
      if (shouldExpand) {
        adapter.insertData(position + 1, model.getSubModels());
      } else {
        for (int i = 0; i < model.getSubModels().size(); ++i) {
          Model item = (Model) adapter.getItem(position + 1);
          if (!CollectionUtils.isEmpty(item.getSubModels())) {
            expand(item, false);
            // NOTE : update data after adapter item change
            item = (Model) adapter.getItem(position + 1);
          }
          adapter.removeData(item);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private int findParentModelPosition(Model model) {
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    int position = adapter.indexOf(model);
    for (int i = position - 1; i >= 0; --i) {
      Model item = (Model) adapter.getItem(i);
      if (!CollectionUtils.isEmpty(item.getSubModels()) && item.getSubModels().contains(model)) {
        return i;
      }
    }
    return -1;
  }

  private Action getValueByViewId(int id, final Model model) {
    switch (id) {
      case 0:
//      case R.id.mask:
        return model.getActions().get(Const.ACTION_MAIN);
      case R.id.fab:
        return model.getActions().get(Const.ACTION_SHOW_HIDE);
    }
    return null;
  }

  @Override
  public void unbind() {
    EarthApplication.getInstance().getRequestManager().cancel(this);
    super.unbind();
  }
}
