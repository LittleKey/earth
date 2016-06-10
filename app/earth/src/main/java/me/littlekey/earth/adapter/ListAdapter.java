package me.littlekey.earth.adapter;

import android.os.Bundle;
import android.view.ViewGroup;

import com.yuanqi.mvp.adapter.MvpAdapter;
import com.yuanqi.mvp.presenter.ViewGroupPresenter;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/10.
 */
public class ListAdapter extends MvpAdapter<Model> {

  private Bundle mBundle;

  public ListAdapter() {
    super();
  }

  public ListAdapter(Bundle bundle) {
    this();
    this.mBundle = bundle;
  }

  @Override
  protected ViewGroupPresenter onCreateDataViewPresenter(ViewGroup parent, int viewType) {
    Model.Template template = Model.Template.values()[viewType];
    switch (template) {
      default:
        throw new IllegalStateException("Nonsupport template : " + template.name());
    }
  }

  @Override
  public int getDataItemViewType(int position) {
    return getItem(position).getTemplate().ordinal();
  }

}
