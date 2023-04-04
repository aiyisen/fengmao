package com.qy.ntf.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qy.ntf.util.JwtTokenUtil;
import com.qy.ntf.util.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class MyShiroRealm extends AuthorizingRealm {

  @Autowired private JwtTokenUtil jwtTokenUtil;
  @Autowired private RedisTemplate redisTemplate;

  /**
   * 使用JWT替代原生token
   *
   * @param token
   * @return boolean
   * @author zhb
   * @date 2020/09/23 20:00
   */
  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JWTToken;
  }

  /** 获取认证信息 */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {

    // 无登录用户，返回空（返回空，将抛出异常）
    if (authenticationToken.getPrincipal() == null) {
      throw new TokenInvalidException("令牌不存在");
    }
    String token = (String) authenticationToken.getCredentials();
    //        String json  = (String) redisTemplate.opsForValue().get(token);
    //        if (StringUtils.isBlank(token)) {
    //            throw new AuthenticationException("无权限");
    //        }
    // 验证是否过期
    //        if (json ==null) {
    //            throw new RuntimeException("令牌已过期");
    //        }
    // jwt取出用户
    Object o = jwtTokenUtil.getObjectFromClaim(token, "USER_INFO");
    UserExt userExt = JSONObject.toJavaObject((JSON) JSON.toJSON(o), UserExt.class);
    Object tokenValue = redisTemplate.opsForValue().get(token);

    /** 创建一个认证对象 */
    return new SimpleAuthenticationInfo(userExt, token, getName());
  }

  /** 获取授权信息 */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    log.info("有接口需要权限认证：{}", principals.getPrimaryPrincipal());
    UserExt userExt = (UserExt) principals.getPrimaryPrincipal();
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

    // 获取用户权限
    Set<String> permissions = new HashSet<>();
    permissions.add("shiro:all");
    info.setStringPermissions(permissions);
    return info;
  }
}
