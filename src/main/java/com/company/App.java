package com.company;

public class App {
  public static void main(String[] args) {
    SqliteConnector connector = new SqliteConnector();
//    connector.dropTable("users_roles");
//    connector.dropTable("users");
//    connector.createUsersTable();
//    connector.createRolesTable();
//    connector.createUsersRolesTable();
//    connector.insertIntoUsersRoles(1, 1);
//    connector.insertIntoUsersRoles(2, 1);
//    connector.insertIntoUsersRoles(3, 1);
//    connector.insertIntoUsersRoles(3, 2);
//    connector.insertIntoUsersRoles(4, 1);
//    connector.insertIntoUsersRoles(4, 2);
//    connector.insertIntoUsersRoles(5, 1);
//    connector.insertIntoUsersRoles(5, 2);
//    connector.insertIntoUsersRoles(5, 3);
//    connector.addAllRoles();
//    User pitt = new User("Brad", "Pitt");
//    pitt.addRole(Role.USER);
//    User norton = new User("Edward", "Norton");
//    norton.addRole(Role.USER);
//    User palahniuk = new User("Chuck", "Palahniuk");
//    palahniuk.addRole(Role.USER);
//    palahniuk.addRole(Role.MANAGER);
//    User uhls = new User("Jim", "Uhls");
//    uhls.addRole(Role.USER);
//    uhls.addRole(Role.MANAGER);
//    User fincher = new User("David", "Fincher");
//    fincher.addRole(Role.USER);
//    fincher.addRole(Role.MANAGER);
//    fincher.addRole(Role.ADMIN);
//    User test = new User("Test", "Test");
//    test.addRole(Role.USER);
//    connector.insertUserIntoUsersTable(test);
//    connector.insertUserIntoUsersTable(pitt);
//    connector.insertUserIntoUsersTable(norton);
//    connector.insertUserIntoUsersTable(palahniuk);
//    connector.insertUserIntoUsersTable(uhls);
//    connector.insertUserIntoUsersTable(fincher);
    connector.printAllRoles();
    connector.printAllUsers();
    connector.printAllFromUsersRoles();
    connector.printRolesByUserId(5);
    connector.printUsersByRole(Role.USER);
    connector.udpateUser(6, "New", "New");
//    System.out.println(connector.deleteUserById(1));
//    System.out.println("After deleting user_id 1");
    connector.printAllUsers();
    connector.printAllFromUsersRoles();
  }
}
/*
user_id: 1, role_id: 1
user_id: 2, role_id: 1
user_id: 3, role_id: 1
user_id: 3, role_id: 2
user_id: 4, role_id: 1
user_id: 4, role_id: 2
user_id: 5, role_id: 1
user_id: 5, role_id: 2
user_id: 5, role_id: 3
 */
