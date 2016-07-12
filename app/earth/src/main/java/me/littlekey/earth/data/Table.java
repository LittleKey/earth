package me.littlekey.earth.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by littlekey on 16/7/13.
 */
public class Table {

  String tableName;

  ArrayList<Column> columnsDefinitions = new ArrayList<>();

  public Table(String table) {
    this(table, BaseColumns._ID, Column.Type.INTEGER);
  }

  public Table(String tableName, String columnsName, Column.Type type) {
    this.tableName = tableName;
    columnsDefinitions.add(new Column(columnsName, Column.Constraint.PRIMARY_KEY, type));
  }

  public Table addColumn(Column columnsDefinition) {
    columnsDefinitions.add(columnsDefinition);
    return this;
  }

  public Table addColumn(String columnName, Column.Type type, String defaultValue) {
    return addColumn(new Column(columnName, null, type, defaultValue));
  }

  public Table addColumn(String columnName, Column.Type type) {
    return addColumn(new Column(columnName, null, type));
  }


  public Table addColumn(String columnName, Column.Constraint constraint, Column.Type type) {
    return addColumn(new Column(columnName, constraint, type));
  }

  public void create(SQLiteDatabase db) {
    final String formatter = " %s";
    final String defaultValueFormatter = " DEFAULT %s";
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
    stringBuilder.append(tableName);
    stringBuilder.append("(");
    int columnCount = columnsDefinitions.size();
    int index = 0;
    for (Column columnsDefinition : columnsDefinitions) {
      stringBuilder.append(columnsDefinition.columnsName)
          .append(String.format(formatter, columnsDefinition.type.name()));

      if (columnsDefinition.defaultValue != null) {
        stringBuilder.append(String.format(defaultValueFormatter, columnsDefinition.defaultValue));
      }
      if (columnsDefinition.constraint != null) {
        stringBuilder.append(String.format(formatter, columnsDefinition.constraint.toString()));
      }
      if (index < columnCount - 1) {
        stringBuilder.append(",");
      }
      index++;
    }
    stringBuilder.append(");");
    db.execSQL(stringBuilder.toString());
  }

  public void delete(final SQLiteDatabase db) {
    db.execSQL("DROP TABLE IF EXISTS " + tableName);
  }
}
