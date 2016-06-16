package me.littlekey.earth.event;

import me.littlekey.earth.model.Model;

/**
 * Created by littlekey on 16/6/17.
 */
public class OnLoadedPageEvent {

  private String baseUrl;
  private Model model;

  public OnLoadedPageEvent(String baseUrl, Model model) {
    this.baseUrl = baseUrl;
    this.model = model;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public Model getModel() {
    return model;
  }
}
