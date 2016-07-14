package me.littlekey.earth.presenter;

import android.app.ActivityOptions;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.wire.Wire;
import com.yuanqi.base.utils.CollectionUtils;
import com.yuanqi.mvp.widget.MvpRecyclerView;

import org.apache.commons.collections4.list.FixedSizeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.littlekey.earth.EarthApplication;
import me.littlekey.earth.R;
import me.littlekey.earth.dialog.LikedDialog;
import me.littlekey.earth.dialog.ProgressDialog;
import me.littlekey.earth.event.OnLikedEvent;
import me.littlekey.earth.event.OnSelectEvent;
import me.littlekey.earth.fragment.AdvancedSearchFragment;
import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.ModelFactory;
import me.littlekey.earth.model.proto.Action;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Flag;
import me.littlekey.earth.network.ApiType;
import me.littlekey.earth.network.EarthRequest;
import me.littlekey.earth.network.EarthResponse;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.DownloadAgent;
import me.littlekey.earth.utils.NavigationManager;
import me.littlekey.earth.utils.ToastUtils;
import me.littlekey.earth.widget.PopupMenuList;
import timber.log.Timber;


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
              Rect rectangle = new Rect();
              Window window = ((FragmentActivity) group().context).getWindow();
              window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                  && !TextUtils.isEmpty(action.transitionName)
                  && view().getTop() > 0 && view().getBottom() <= rectangle.height()) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    (FragmentActivity) view().getContext(), view().findViewById(R.id.cover), action.transitionName);
                NavigationManager.navigationTo(view().getContext(), action.uri, action.bundle, options);
              } else {
                NavigationManager.navigationTo(view().getContext(), action.uri, action.bundle);
              }
            } else if (null != action.url) {
              NavigationManager.navigationTo(view().getContext(), action.url, action.bundle);
            }
            break;
          case JUMP_IMAGE:
            if (action.uri != null) {
              Bundle bundle = action.bundle;
              if (bundle == null) {
                bundle = new Bundle();
              }
              bundle.putStringArrayList(Const.KEY_TOKEN_LIST, getTokenList(model));
              NavigationManager.navigationTo(view().getContext(), action.uri, bundle);
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
            if (action.clazz != null) {
              try {
                EventBus.getDefault().post(
                    action.clazz.getDeclaredConstructor(Model.class).newInstance(model));
              } catch (IllegalAccessException e) {
                Timber.e(e, "IllegalAccessException in ActionPresenter");
              } catch (NoSuchMethodException e) {
                Timber.e(e, "NoSuchMethodException in ActionPresenter");
              } catch (InstantiationException e) {
                Timber.e(e, "InstantiationException in ActionPresenter");
              } catch (InvocationTargetException e) {
                Timber.e(e, "InvocationTargetException in ActionPresenter");
              }
            }
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
          case SELECT_CATEGORY:
            selectCategory(model);
            break;
          case DOWNLOAD:
            download(model);
            break;
          case SELECT_ADV_OPT:
            selectAdvOpt(model);
            break;
          case POPUP_RATING:
            popupRating(model);
            break;
        }
      }
    });
  }

  @SuppressWarnings("unchecked")
  private void popupRating(Model model) {
    final MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    final int position = adapter.indexOf(model);
    PopupMenuList popupMenuList = new PopupMenuList(view().getContext());
    popupMenuList.setItems(AdvancedSearchFragment.sRatingsReadOnlyList);
    popupMenuList.setOnDismissListener(new PopupMenuList.OnDismissListener() {
      @Override
      public void onDismiss(int chosenPosition) {
        if (chosenPosition > -1) {
          Model item = adapter.getItem(position);
          adapter.changeData(position, item.newBuilder()
              .count(item.count.newBuilder().rating((float) (chosenPosition + 2)).build())
              .build());
        }
      }
    });
    popupMenuList.showAsDropDown(view());
  }

  @SuppressWarnings("unchecked")
  private void selectAdvOpt(Model model) {
    MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    int position = adapter.indexOf(model);
    // NOTE : not check null value
    adapter.changeData(position, model.newBuilder()
        .flag(model.flag.newBuilder().is_selected(!model.flag.is_selected).build())
        .build());
  }

  @SuppressWarnings("unchecked")
  private void download(final Model model) {
    DownloadAgent agent = EarthApplication.getInstance()
        .newDownload(ModelFactory.createDLCModelFromArt(model.art, Model.Template.ITEM_DLC));
    if (model.template == Model.Template.ITEM_DLC) {
      final MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
      final int index = adapter.indexOf(model);
      agent.addListener(new DownloadAgent.ListenerAdapter() {
        @Override
        public void onProgress(float progress) {
          adapter.changeData(index, model.newBuilder()
              .template(Model.Template.ITEM_DLC_DOWNLOADING)
              .count(model.count.newBuilder().progress(progress).build())
              .build());
        }

        @Override
        public void onComplete(boolean succeed) {
          adapter.changeData(index, model.newBuilder()
              .template(Model.Template.ITEM_DLC)
              .build());
        }
      });
    }
    agent.connect();
  }

  @SuppressWarnings("unchecked")
  private ArrayList<String> getTokenList(Model model) {
    MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    FixedSizeList<String> tokens = FixedSizeList.fixedSizeList(Arrays.asList(new String[model.count.pages]));
    for (Model item: adapter.getData()) {
      List<String> paths = Uri.parse(item.url).getPathSegments();
      int pos = Wire.get(item.count.number, 0) - 1;
      if (!CollectionUtils.isEmpty(paths) && pos >= 0 && pos < adapter.size()) {
        tokens.set(pos, paths.get(1));
      }
    }
    return new ArrayList<>(tokens);
  }

  private void logout() {
    EarthApplication.getInstance().getAccountManager().logout();
  }

  private void liked(Model model, final String gid, String token) {
    final boolean isUnlike = model.identity.equals(Const.FAV_DEL);
    final ProgressDialog dialog = new ProgressDialog(group().context);
    dialog.show();
    Map<String, String> query = new HashMap<>();
    query.put(Const.KEY_GID, gid);
    query.put(Const.KEY_T, token);
    query.put(Const.KEY_ACT, Const.ADD_FAV);
    Map<String, String> params = new HashMap<>();
    params.put(Const.KEY_FAV_NOTE, Const.EMPTY_STRING);
    params.put(Const.KEY_UPDATE, Const.ONE);
    params.put(Const.KEY_FAV_CAT, model.identity);
    params.put(Const.KEY_APPLY, model.description);
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

  private void selectCategory(Model model) {
    @SuppressWarnings("unchecked")
    MvpRecyclerView.Adapter<Model> adapter = group().pageContext.adapter;
    int position = adapter.indexOf(model);
    if (position != -1) {
      Model oldItem = adapter.getItem(position);
      Flag flag = oldItem.flag;
      adapter.changeData(position, oldItem.newBuilder()
          .flag(flag.newBuilder().is_selected(!flag.is_selected).build())
          .build());
      if (flag.is_selected) {
        EarthApplication.getInstance().removeSelectCategory(model.category);
      } else {
        EarthApplication.getInstance().addSelectCategory(model.category);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void select(Model model, boolean updateParent) {
    boolean willSelect = model.flag == null || !Wire.get(model.flag.is_selected, false);
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    Flag flag = Wire.get(model.flag, new Flag.Builder().build()).newBuilder()
        .is_selected(willSelect).build();
    Model newModel = model.newBuilder().flag(flag).build();
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
        Wire.get(model.flag, new Flag.Builder().build()).newBuilder().is_selected(true)
            .build();
    Model newModel = model.newBuilder().flag(flag).build();
    for (int i = 0; i < adapter.getData().size(); i++) {
      Object item = adapter.getItem(i);
      if (item instanceof Model && (flag = ((Model) item).flag) != null
          && Wire.get(flag.is_selected, false) && !item.equals(model)) {
        flag = flag.newBuilder().is_selected(false).build();
        adapter.changeData(i, ((Model) item).newBuilder().flag(flag).build());
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
      List<Model> subModels = new ArrayList<>(parent.subModels);
      subModels.set(subModels.indexOf(model), newModel);
      int selectedNum = 0;
      for (Model sub : subModels) {
        Flag flag = sub.flag;
        if (!CollectionUtils.isEmpty(sub.subModels)) {
          selectedNum += sub.count != null ? Wire.get(sub.count.selected_num, 0) : 0;
        } else if (flag != null && Wire.get(flag.is_selected, false)) {
          ++selectedNum;
        }
      }
      Count count = new Count.Builder().selected_num(selectedNum).build();
      Model newParent = parent.newBuilder().subModels(subModels).count(count).build();
      updateParentSubModels(adapter, parent, newParent);
      adapter.changeData(parentPosition, newParent);
    }
  }

  @SuppressWarnings({"unchecked", "unused"})
  private void expand(Model model, boolean shouldExpand) {
    MvpRecyclerView.Adapter adapter = group().pageContext.adapter;
    // int position = group().holder.getAdapterPosition();
    int position = adapter.indexOf(model);
    if (shouldExpand != (model.flag != null && Wire.get(model.flag.is_selected, false))) {
      Model newModel = model.newBuilder().flag(
          new Flag.Builder().is_selected(shouldExpand).build()).build();
      updateParentSubModels(adapter, model, newModel);
      adapter.changeData(position, newModel);
      if (shouldExpand) {
        adapter.insertData(position + 1, model.subModels);
      } else {
        for (int i = 0; i < model.subModels.size(); ++i) {
          Model item = (Model) adapter.getItem(position + 1);
          if (!CollectionUtils.isEmpty(item.subModels)) {
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
      if (!CollectionUtils.isEmpty(item.subModels) && item.subModels.contains(model)) {
        return i;
      }
    }
    return -1;
  }

  private Action getValueByViewId(int id, final Model model) {
    switch (id) {
      case 0:
//      case R.id.mask:
        return model.actions.get(Const.ACTION_MAIN);
      case R.id.fab:
        return model.actions.get(Const.ACTION_SHOW_HIDE);
      case R.id.likes:
        return model.actions.get(Const.ACTION_LIKED);
      case R.id.cover:
      case R.id.re_download:
        return model.actions.get(Const.ACTION_DOWNLOAD);
    }
    return null;
  }

  @Override
  public void unbind() {
    EarthApplication.getInstance().getRequestManager().cancel(this);
    super.unbind();
  }
}
