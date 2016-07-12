package me.littlekey.earth.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by littlekey on 16/7/13.
 */
public class QuickSearchUtils {

  public static void append(ContentResolver resolver, String name, String url) {
    Cursor cursor = queryAll(resolver);
    int lastOrder = 0;
    if (cursor != null && cursor.moveToLast()) {
      lastOrder = cursor.getInt(cursor.getColumnIndex(DataContract.QuickSearch.COLUMN_NUMBER));
      cursor.close();
    }
    ContentValues contentValues = new ContentValues();
    contentValues.put(DataContract.QuickSearch.COLUMN_NAME, name);
    contentValues.put(DataContract.QuickSearch.COLUMN_URL, url);
    contentValues.put(DataContract.QuickSearch.COLUMN_NUMBER, lastOrder + 1);
    resolver.insert(DataContract.QuickSearch.CONTENT_URI, contentValues);
  }

  public static Cursor queryAll(ContentResolver resolver) {
    return resolver.query(DataContract.QuickSearch.CONTENT_URI,
        new String[] {
            DataContract.QuickSearch.COLUMN_NAME,
            DataContract.QuickSearch.COLUMN_URL,
            DataContract.QuickSearch.COLUMN_NUMBER,
        }, null, null, DataContract.QuickSearch.COLUMN_NUMBER);
  }
}
