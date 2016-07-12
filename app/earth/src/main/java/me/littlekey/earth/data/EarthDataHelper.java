package me.littlekey.earth.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by littlekey on 16/7/13.
 */
public class EarthDataHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "earth.db";
  private static final int DATABASE_VERSION = 1;

  public EarthDataHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    new Table(DataContract.QuickSearch.TABLE_NAME)
        .addColumn(DataContract.QuickSearch.COLUMN_NAME, Column.Constraint.UNIQUE, Column.Type.TEXT)
        .addColumn(DataContract.QuickSearch.COLUMN_URL, Column.Constraint.UNIQUE, Column.Type.TEXT)
        .addColumn(DataContract.QuickSearch.COLUMN_NUMBER, Column.Constraint.UNIQUE, Column.Type.INTEGER)
        .create(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
