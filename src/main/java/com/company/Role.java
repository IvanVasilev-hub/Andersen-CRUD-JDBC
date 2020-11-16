package com.company;

public enum Role {
  USER(1),
  MANAGER(2),
  ADMIN(3);
  private int roleId;

  Role(int roleId) {
    this.roleId = roleId;
  }

  public int getRoleId() {
    return roleId;
  }
}
