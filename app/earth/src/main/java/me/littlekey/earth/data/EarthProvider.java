package me.littlekey.earth.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by littlekey on 16/7/13.
 */
public class EarthProvider extends ContentProvider {

  private static final int ROUTE_EARTH_LIST = 0;
  private static final int ROUTE_EARTH_ITEM = 1;

  private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY, "quick_search", ROUTE_EARTH_LIST);
    URI_MATCHER.addURI(DataContract.CONTENT_AUTHORITY, "quick_search/*", ROUTE_EARTH_ITEM);
  }

  private EarthDataHelper mDatabaseHelper;

  @Override
  public boolean onCreate() {
    mDatabaseHelper = new EarthDataHelper(getContext());
    return true;
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    final int match = URI_MATCHER.match(uri);
    switch (match) {
      case ROUTE_EARTH_LIST:
        return DataContract.QuickSearch.CONTENT_TYPE;
      case ROUTE_EARTH_ITEM:
        return DataContract.QuickSearch.CONTENT_ITEM_TYPE;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  @Override
  public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
    SelectionBuilder builder = new SelectionBuilder();
    assert db != null;
    final int match = URI_MATCHER.match(uri);
    int result;
    switch (match) {
      case ROUTE_EARTH_ITEM:
        String name = uri.getLastPathSegment();
        result = builder.table(DataContract.QuickSearch.TABLE_NAME)
            .where(DataContract.QuickSearch.COLUMN_NAME + "=?", name)
            .where(selection, selectionArgs)
            .update(db, values);
        break;
      default:
        throw new UnsupportedOperationException("Not support uri: " + uri);
    }
    return result;
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, ContentValues values) {
    final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
    assert db != null;
    final int match = URI_MATCHER.match(uri);
    Uri result;
    long id;
    switch (match) {
      case ROUTE_EARTH_LIST:
        id = db.insertWithOnConflict(DataContract.QuickSearch.TABLE_NAME, null, values,
            SQLiteDatabase.CONFLICT_REPLACE);
        result = Uri.parse(DataContract.QuickSearch.CONTENT_URI + "/" + id);
        break;
      default:
        throw new UnsupportedOperationException("Not support uri: " + uri);
    }
    if (getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null, false);
    }
    return result;
  }

  @Override
  public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
    assert db != null;
    final int match = URI_MATCHER.match(uri);
    int result;
    switch (match) {
      case ROUTE_EARTH_LIST:
        result = db.delete(DataContract.QuickSearch.TABLE_NAME, selection, selectionArgs);
        break;
      default:
        throw new UnsupportedOperationException("Not support uri: " + uri);
    }
    if (getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null, false);
    }
    return result;
  }

  @Nullable
  @Override
  public Cursor query(@NonNull  Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
    SelectionBuilder builder = new SelectionBuilder();
    assert db != null;
    final int match = URI_MATCHER.match(uri);
    Cursor cursor;
    String name;
    switch (match) {
      case ROUTE_EARTH_ITEM:
        name = uri.getLastPathSegment();
        builder.where(DataContract.QuickSearch.COLUMN_NAME + "=?", name);
      case ROUTE_EARTH_LIST:
        cursor = builder.table(DataContract.QuickSearch.TABLE_NAME)
            .where(selection, selectionArgs)
            .query(db, projection, sortOrder);
        break;
      default:
        throw new UnsupportedOperationException("Not support uri: " + uri);
    }
    assert getContext() != null;
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }
}
