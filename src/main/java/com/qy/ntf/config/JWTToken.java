package com.qy.ntf.config;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义JWT的token
 *
 * @author zhb
 * @date 2020/09/23 19:22
 **/
public class JWTToken implements AuthenticationToken {

    /**
     * 密钥
     */
    private String token;

    /**
     * 构造函数
     *
     * @author zhb
     * @date 2020/09/23 19:25
     * @param token
     * @return
     */
    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
