package com.qy.ntf.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserExt implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String roleId;
}
