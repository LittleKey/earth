package me.littlekey.earth.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by littlekey on 16/7/13.
 */
public class SelectionBuilder {

  private String mTable = null;
  private Map<String, String> mProjectionMap = new HashMap<>();
  private StringBuilder mSelection = new StringBuilder();
  private ArrayList<String> mSelectionArgs = new ArrayList<>();

  public static void appendColumns(StringBuilder s, String[] columns) {
    int n = columns.length;

    for (int i = 0; i < n; ++i) {
      String column = columns[i];
      if (column != null) {
        if (i > 0) {
          s.append(", ");
        }
        s.append(column);
      }
    }
    s.append(' ');
  }

  public SelectionBuilder reset() {
    mTable = null;
    mSelection.setLength(0);
    mSelectionArgs.clear();
    return this;
  }

  public SelectionBuilder where(String selection, String... selectionArgs) {
    if (TextUtils.isEmpty(selection)) {
      if (selectionArgs != null && selectionArgs.length > 0) {
        throw new IllegalArgumentException("Valid selection required when including arguments=");
      }
      return this;
    }
    if (mSelection.length() > 0) {
      mSelection.append(" AND ");
    }
    mSelection.append("(").append(selection).append(")");
    if (selectionArgs != null) {
      Collections.addAll(mSelectionArgs, selectionArgs);
    }
    return this;
  }

  public SelectionBuilder table(String table) {
    mTable = table;
    return this;
  }

  private void assertTable() {
    if (mTable == null) {
      throw new IllegalStateException("Table not specified");
    }
  }

  public SelectionBuilder mapTpTable(String column, String table) {
    mProjectionMap.put(column, table + "." + column);
    return this;
  }

  public SelectionBuilder map(String fromColumn, String toClause) {
    mProjectionMap.put(fromColumn, toClause + "AS" + fromColumn);
    return this;
  }

  public String getSelection() {
    return mSelection.toString();
  }

  public String[] getSelectionArgs() {
    return mSelectionArgs.toArray(new String[mSelectionArgs.size()]);
  }

  private void mapColumns(String[] columns) {
    for (int i = 0; i < columns.length; ++i) {
      final String target = mProjectionMap.get(columns[i]);
      if (target != null) {
        columns[i] = target;
      }
    }
  }

  @Override
  public String toString() {
    return "SelectionBuilder[table=" + mTable + ", selection=" + getSelection()
        + ", selectionArgs=" + Arrays.toString(getSelectionArgs()) + "]";
  }

  public Cursor rawQuery(SQLiteDatabase db, String[] columns, String[] tables, String[] foreignKey,
                         String orderBy) {
    assertTable();
    String formatter = " %s";
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    if (columns != null && columns.length != 0) {
      appendColumns(builder, columns);
    } else {
      builder.append("* ");
    }
    builder.append("FROM").append(String.format(formatter, mTable));
    assert tables != null;
    assert foreignKey != null;
    if (tables.length != foreignKey.length) {
      throw new AssertionError();
    }
    for (int i = 0; i < tables.length; i++) {
      builder.append(" LEFT JOIN").append(String.format(formatter, tables[i]))
          .append(" ON")
          .append(String.format(" %s.%s=%s.%s", mTable, foreignKey[i], tables[i], foreignKey[i]));
    }
    builder.append(!(TextUtils.isEmpty(getSelection())) ? " WHERE " + getSelection() : "")
        .append(!(TextUtils.isEmpty(orderBy)) ? " ORDER BY " + orderBy : "");
    return db.rawQuery(builder.toString(), getSelectionArgs());
  }

  /**
   * Execute query using the current internal state as {@code WHERE} clause.
   *
   * @return Cursor.
   */
  public Cursor query(SQLiteDatabase db, String[] columns, String orderBy) {
    return query(db, columns, null, null, orderBy, null);
  }

  /**
   * Execute query using the current internal state as {@code WHERE} clause.
   *
   * @return Cursor.
   */
  public Cursor query(SQLiteDatabase db, String[] columns, String groupBy,
                      String having, String orderBy, String limit) {
    assertTable();
    if (columns != null) {
      mapColumns(columns);
    }
    Timber.v("query(columns=" + Arrays.toString(columns) + ") " + this);
    return db.query(mTable, columns, getSelection(), getSelectionArgs(), groupBy, having,
        orderBy, limit);
  }

  /**
   * Execute update using the current internal state as {@code WHERE} clause.
   *
   * @return num updated.
   */
  public int update(SQLiteDatabase db, ContentValues values) {
    assertTable();
    Timber.v("update() " + this);
    return db.update(mTable, values, getSelection(), getSelectionArgs());
  }

  /**
   * Execute delete using the current internal state as {@code WHERE} clause.
   *
   * @return num deleted.
   */
  public int delete(SQLiteDatabase db) {
    assertTable();
    Timber.v("delete() " + this);
    return db.delete(mTable, getSelection(), getSelectionArgs());
  }
}
