package me.littlekey.earth.network;

import org.jsoup.nodes.Document;

import java.util.Map;

/**
 * Created by littlekey on 16/6/12.
 */
public class EarthResponse {

  public final Document document;

  public final Map<String, String> headers;

  public EarthResponse(Document document, Map<String, String> headers) {
    this.document = document;
    this.headers = headers;
  }
}
