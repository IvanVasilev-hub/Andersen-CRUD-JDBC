package com.company;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqliteConnectorTest {
  private static SqliteConnector connector = new SqliteConnector();
  private static Connection connection;

  @BeforeAll
  public static void init() throws SQLException {
    connection = connector.getNewConnection();
  }

  @AfterAll
  public static void close() throws SQLException {
    connection.close();
  }

  @Test
  void testGetNewConnection() {
    try {
      assertTrue(connection.isValid(1));
      assertFalse(connection.isClosed());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
