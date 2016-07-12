package me.littlekey.earth.data;

/**
 * Created by littlekey on 16/7/13.
 */
public class Column {

  public final String columnsName;
  public final Constraint constraint;
  public final Type type;
  public final String defaultValue;

  public Column(String columnsName, Constraint constraint, Type type) {
    this(columnsName, constraint, type, null);
  }

  public Column(String columnsName, Constraint constraint, Type type, String defaultValue) {
    this.columnsName = columnsName;
    this.constraint = constraint;
    this.type = type;
    this.defaultValue = defaultValue;
  }

  public enum Constraint {
    UNIQUE("UNIQUE"),
    NOT("NOT"),
    NULL("NULL"),
    CHECK("CHECK"),
    FOREIGN_KEY("FOREIGN KEY"),
    PRIMARY_KEY("PRIMARY KEY");

    private String value;

    Constraint(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public enum Type {
    NULL, INTEGER, REAL, TEXT, BLOB
  }
}
