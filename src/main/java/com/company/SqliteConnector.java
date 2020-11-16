package com.company;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.List;

public class SqliteConnector {
  private String url = "jdbc:sqlite:";
  private SQLiteDataSource source;
  public static final String TABLE_USERS = "users";
  public static final String TABLE_ROLES = "roles";
  public static final String TABLE_USERS_ROLES = "users_roles";

  public SqliteConnector(String dbName) {
    source = new SQLiteDataSource();
    source.setUrl(url + dbName);
  }

  public SqliteConnector() {
    this("sample.db");
  }

  public void connectToDataBase() {
    connectToDataBase("sample.db");
  }

  public void connectToDataBase(String dbName) {
    try (Connection conn = DriverManager.getConnection(url + dbName)) {
      if (conn != null) {
        DatabaseMetaData meta = conn.getMetaData();
        System.out.println("The driver name is " + meta.getDriverName());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public Connection getNewConnection() throws SQLException {
    return source.getConnection();
  }

  public void createUsersTable() {
    try (Connection conn = source.getConnection();
         Statement statement = conn.createStatement()) {
      statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_USERS +
          "(user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
          "first_name TEXT NOT NULL," +
          "last_name TEXT NOT NULL" +
          ");");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void createRolesTable() {
    try (Connection conn = getNewConnection();
         Statement statement = conn.createStatement()) {
      statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_ROLES +
          "(role_id INTEGER PRIMARY KEY," +
          "role TEXT UNIQUE NOT NULL" +
          ");"
      );
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void createUsersRolesTable() {
    try (Connection conn = getNewConnection();
         Statement statement = conn.createStatement()) {
      statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_USERS_ROLES +
          "(user_id INTEGER NOT NULL REFERENCES users(user_id), " +
          "role_id INTEGER NOT NULL REFERENCES roles(role_id), " +
          "CONSTRAINT users_roles_pk PRIMARY KEY (user_id, role_id)" +
          ")");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void insertUserIntoUsersTable(User user) {
    String query = "INSERT INTO " + TABLE_USERS + " (first_name, last_name) VALUES (?, ?)";
    try (Connection conn = source.getConnection();
         PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setString(1, user.getFirstName());
      statement.setString(2, user.getLastName());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    int userId = getUserIdByFirstAndLastName(user.getFirstName(), user.getLastName());
    if (userId != -1) {
      insertUserAndRoleIdInUsersRolesTable(userId, user.getRoles());
    }
  }

  public int getUserIdByFirstAndLastName(String firstName, String lastName) {
    String query = "SELECT user_id FROM " + TABLE_USERS
        + " WHERE first_name = ? AND last_name = ?";
    int userId = -1;
    try (Connection conn = source.getConnection();
         PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setString(1, firstName);
      statement.setString(2, lastName);
      ResultSet set = statement.executeQuery();
      userId = set.getInt(1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return userId;
  }

  public void insertUserAndRoleIdInUsersRolesTable(int user_id, List<Role> roles) {
    String query = "INSERT INTO " + TABLE_USERS_ROLES + " VALUES (?, ?)";
    try (Connection conn = source.getConnection();
         PreparedStatement statement = conn.prepareStatement(query)) {
      for (Role role : roles) {
        statement.setInt(1, user_id);
        statement.setInt(2, role.getRoleId());
        statement.execute();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void insertIntoUsersRoles(int userId, int roleId) {
    String query = "INSERT INTO " + TABLE_USERS_ROLES + " VALUES (?, ?)";
    try (Connection conn = source.getConnection();
         PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setInt(1, userId);
      statement.setInt(2, roleId);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addAllRoles() {
    String query = "INSERT INTO " + TABLE_ROLES + " (role_id, role) VALUES (?, ?)";
    try (Connection conn = source.getConnection();
         PreparedStatement statement = conn.prepareStatement(query)) {
      for (Role role : Role.values()) {
        statement.setInt(1, role.getRoleId());
        statement.setString(2, String.valueOf(role));
        statement.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void printRolesByUserId(int userId) {
    String query = "SELECT users.user_id, roles.role FROM " + TABLE_USERS
        + " JOIN " + TABLE_USERS_ROLES + " USING (user_id) "
        + " JOIN " + TABLE_ROLES + " USING (role_id) "
        + " WHERE users.user_id = ?";
    try (Connection conn = source.getConnection();
         PreparedStatement st = conn.prepareStatement(query)) {
      st.setInt(1, userId);
      ResultSet set = st.executeQuery();
      while (set.next()) {
        System.out.println("user_id: " + set.getInt(1) + ", role: " + set.getString(2));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void printUsersByRole(Role role) {
    String query = "SELECT users.first_name, users.last_name FROM " + TABLE_USERS
        + " JOIN " + TABLE_USERS_ROLES + " USING (user_id) "
        + " JOIN " + TABLE_ROLES + " USING (role_id) "
        + " WHERE roles.role = ?";
    try (Connection conn = source.getConnection();
         PreparedStatement st = conn.prepareStatement(query)) {
      st.setString(1, String.valueOf(role));
      ResultSet set = st.executeQuery();
      System.out.println("Role " + role);
      while (set.next()) {
        System.out.println(set.getString(1) + " " + set.getString(2));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void printAllRoles() {
    String query = "SELECT * FROM " + TABLE_ROLES;
    try (Connection conn = source.getConnection();
         Statement statement = conn.createStatement();
         ResultSet set = statement.executeQuery(query)) {
      while (set.next()) {
        System.out.println("role_id: " + set.getInt("role_id") +
            ", role: " + set.getString("role"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void printAllUsers() {
    String query = "SELECT * FROM " + TABLE_USERS;
    try (Connection conn = source.getConnection();
         Statement statement = conn.createStatement();
         ResultSet set = statement.executeQuery(query)) {
      while (set.next()) {
        System.out.println("user_id: " + set.getInt("user_id") +
            ", full name: " + set.getString("first_name") + " " + set.getString("last_name"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void printAllFromUsersRoles() {
    String query = "SELECT * FROM " + TABLE_USERS_ROLES;
    try (Connection conn = source.getConnection();
         Statement statement = conn.createStatement();
         ResultSet set = statement.executeQuery(query)) {
      while (set.next()) {
        System.out.println("user_id: " + set.getInt(1) +
            ", role_id: " + set.getInt(2));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int deleteUserById(int userId) {
    String query = "DELETE FROM " + TABLE_USERS
        + " WHERE user_id = ?";
    int result = -1;
    try (Connection conn = source.getConnection();
         PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setInt(1, userId);
      result = statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (result > 0) {
      deleteFromUsersRolesByUserId(userId);
    }
    return result;
  }

  public int deleteFromUsersRolesByUserId(int userId) {
    String query = "DELETE FROM " + TABLE_USERS_ROLES
        + " WHERE user_id = ?";
    try (Connection conn = source.getConnection();
         PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setInt(1, userId);
      return statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public int udpateUser(int userId, String firstName, String lastName) {
    String query = "UPDATE " + TABLE_USERS + " SET first_name = ?, last_name = ? " +
        "WHERE user_id = ? ";
    try (Connection conn = source.getConnection();
         PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setString(1, firstName);
      statement.setString(2, lastName);
      statement.setInt(3, userId);
      return statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public void dropTable(String tableName) {
    String query = "DROP TABLE " + tableName;
    try (Connection conn = getNewConnection();
         Statement st = conn.createStatement()) {
      st.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
