package com.qy.ntf.bean.dto;

public class UserData {
  private Integer userId;
  private String username;
  private String realname;
  private String organizationCode;
  public String bankCard;

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getRealname() {
    return realname;
  }

  public void setRealname(String realname) {
    this.realname = realname;
  }

  public String getOrganizationCode() {
    return organizationCode;
  }

  public void setOrganizationCode(String organizationCode) {
    this.organizationCode = organizationCode;
  }

  @Override
  public String toString() {
    return "UserData [userId="
        + userId
        + ", username="
        + username
        + ", realname="
        + realname
        + ", organizationCode="
        + organizationCode
        + "]";
  }
}
