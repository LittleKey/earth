package me.littlekey.earth.presenter;

import android.app.ActivityOptions;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.wire.Wire;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.mvp.widget.MvpRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.dialog.LikedDialog;
import me.littlekey.earth.dialog.ProgressDialog;
import me.littlekey.earth.event.OnClickTagItemEvent;
import me.littlekey.earth.event.OnLikedEvent;
import me.littlekey.earth.event.OnSelectEvent;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.NavigationManager;
import me.littlekey.earth.utils.ToastUtils;


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
          case LIKED:
            if (action.bundle != null) {
              liked(model, action.bundle.getString(Const.KEY_GID),
                  action.bundle.getString(Const.KEY_TOKEN));
            }
            break;
          case SELECT_FAV:
            if (action.bundle != null) {
              selectFav(action.bundle.getString(Const.KEY_GID),
                  action.bundle.getString(Const.KEY_TOKEN));
            }
            break;
        }
      }
    });
  }


  private void logout() {
    EarthApplication.getInstance().getAccountManager().logout();
  }

  private void liked(Model model, final String gid, String token) {
    final boolean isUnlike = model.getIdentity().equals(Const.FAV_DEL);
    final ProgressDialog dialog = new ProgressDialog(group().context);
    dialog.show();
    Map<String, String> query = new HashMap<>();
    query.put(Const.KEY_GID, gid);
    query.put(Const.KEY_T, token);
    query.put(Const.KEY_ACT, Const.ADD_FAV);
    Map<String, String> params = new HashMap<>();
    params.put(Const.KEY_FAV_NOTE, Const.EMPTY_STRING);
    params.put(Const.KEY_UPDATE, Const.ONE);
    params.put(Const.KEY_FAV_CAT, model.getIdentity());
    params.put(Const.KEY_APPLY, model.getDescription());
    EarthRequest request = EarthApplication.getInstance().getRequestManager()
        .newEarthRequest(ApiType.LIKED, Request.Method.POST, new Response.Listener<EarthResponse>() {
          @Override
          public void onResponse(EarthResponse response) {
            EventBus.getDefault().post(new OnLikedEvent(gid, !isUnlike));
            ToastUtils.toast(isUnlike ? R.string.unlike_succeed : R.string.like_succeed);
            dialog.dismiss();
          }
        }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            EventBus.getDefault().post(new OnLikedEvent(gid, isUnlike));
            ToastUtils.toast(isUnlike ? R.string.unlike_error : R.string.like_succeed);
            dialog.dismiss();
          }
        });
    request.setTag(this);
    request.setQuery(query);
    request.setParams(params);
    request.submit();
  }

  private void selectFav(String gid, String token) {
    LikedDialog.newInstance(gid, token).show(group().context);
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
      case R.id.likes:
        return model.getActions().get(Const.ACTION_LIKED);
    }
    return null;
  }

  @Override
  public void unbind() {
    EarthApplication.getInstance().getRequestManager().cancel(this);
    super.unbind();
  }
}
