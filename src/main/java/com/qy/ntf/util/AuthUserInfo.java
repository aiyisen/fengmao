package com.qy.ntf.util;


/**
 * @Author: Eric
 * @Date: 2021/2/2 20:07
 */
public class AuthUserInfo {

    public AuthUserInfo(Integer userId, String username, String realname, String organizationCode) {
        this.userId = userId;
        this.username = username;
        this.realname = realname;
        this.organizationCode = organizationCode;
    }

    private Integer userId;
    private String username;
    private String realname;
    private String organizationCode;

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
}