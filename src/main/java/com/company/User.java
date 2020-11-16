package com.company;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class User {
  private String firstName;
  private String lastName;
  private List<Role> roles;

  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    roles = new LinkedList<>();
  }

  public boolean addRole(Role role) {
    if (!roles.contains(role)) {
      return roles.add(role);
    }
    return false;
  }

  public boolean removeRole(Role role) {
    return roles.remove(role);
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<Role> getRoles() {
    return Collections.unmodifiableList(roles);
  }
}
