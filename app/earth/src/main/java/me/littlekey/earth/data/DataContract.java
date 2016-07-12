package me.littlekey.earth.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by littlekey on 16/7/13.
 */
public class DataContract {

  public static final String CONTENT_AUTHORITY = "me.littlekey.earth";

  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static final String PATH_QUICK_SEARCH = "quick_search";

  public static class QuickSearch implements BaseColumns {

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.littlekey.earth.quick_search_list";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.littlekey.earth.search";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUICK_SEARCH).build();

    public static final String TABLE_NAME = "quick_search";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_URL = "url";

    public static final String COLUMN_NUMBER = "number";
  }
}
