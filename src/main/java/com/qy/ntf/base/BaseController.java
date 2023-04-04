package com.qy.ntf.base;

import com.alibaba.fastjson.JSONObject;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.util.TokenInvalidException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class BaseController {
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private HttpServletRequest request;
  private String header = "Authorization";

  public UserDto getUserData() {
    String headerValue = request.getHeader(header);
    if (Strings.isNotEmpty(headerValue)) {
      String json = redisTemplate.opsForValue().get(headerValue);
      if (json != null) {
        return JSONObject.parseObject(json, UserDto.class);
      } else {
        throw new TokenInvalidException("令牌失效", -2);
      }
    }
    return null;
  }

  public SysUserAdminDto getAdminUserData() {
    String headerValue = request.getHeader(header);
    if (headerValue != null) {
      String json = redisTemplate.opsForValue().get(headerValue);
      if (json != null) {
        return JSONObject.parseObject(json, SysUserAdminDto.class);
      }
    }
    return null;
  }

  public String getToken() {
    return request.getHeader(header);
  }

  // 是否超级管理员
  public boolean isSuperMan() {
    SysUserAdminDto userData = getAdminUserData();

    if (userData != null && userData.getId().equals(1L)) {
      return true;
    }

    return false;
  }

  public void setUpdateUserInfo(BaseEntity param) {
    UserDto userData = this.getUserData();
    param.setUpdateId(userData.getId());
    param.setUpdateTime(new Date());
  }

  public void setCreateUserInfo(BaseEntity param) {
    UserDto userData = this.getUserData();
    param.setCreateId(userData.getId());
    param.setCreateTime(new Date());
  }
}
