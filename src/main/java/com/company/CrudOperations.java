package com.company;

import java.sql.Connection;
import java.sql.SQLException;

public class CrudOperations {
  private SqliteConnector connector;
  private Connection connection;

  public CrudOperations(SqliteConnector connector) {
    this.connector = connector;
    setConnection();
  }

  private void setConnection() {
    try {
      connection = connector.getNewConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
