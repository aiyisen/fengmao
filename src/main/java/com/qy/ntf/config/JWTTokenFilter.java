package com.qy.ntf.config;

import com.qy.ntf.util.IPUtil;
import com.qy.ntf.util.TokenInvalidException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JWTTokenFilter extends BasicHttpAuthenticationFilter {
  @Autowired private RedisTemplate<String, String> redisTemplate;
  /** 登录标识 */
  private static final String LOGIN_SIGN = "Authorization";

  public void setRedisTemplate(RedisTemplate<String, String> re) {
    this.redisTemplate = re;
  }

  /**
   * 检测用户是否登录 检测header里面是否包含登录标识（LOGIN_SIGN）
   *
   * @author zhb
   * @date 2020/09/23 19:49
   * @param request request
   * @param response response
   * @return boolean
   */
  @Override
  protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
    HttpServletRequest req = (HttpServletRequest) request;
    String authorization = req.getHeader(LOGIN_SIGN);
    log.info("Filter，isLoginAttempt=======================捕捉路径{}", req.getRequestURI());
    // 获取访问者的ip
    String ipAdress = IPUtil.getIpAddress((HttpServletRequest) request);
    log.debug("=========访问列表页的ip地址为:[{}]", ipAdress);
    // 将ip存入redis
    HyperLogLogOperations<String, String> hyperlog = redisTemplate.opsForHyperLogLog();
    hyperlog.add("cpp_bank_list_total_size_today", ipAdress);

    if (authorization == null) {
      request.setAttribute("tokenOut", "error");
      throw new RuntimeException("令牌缺失");
    } else {
      String userJson = redisTemplate.opsForValue().get(authorization);
      if (Strings.isEmpty(userJson)) {
        request.setAttribute("tokenOut", "无效的token");
        throw new RuntimeException("无效的token");
      }
    }
    return Boolean.TRUE.equals(redisTemplate.hasKey(authorization));
  }

  @Override
  protected boolean executeLogin(ServletRequest request, ServletResponse response)
      throws Exception {
    HttpServletRequest req = (HttpServletRequest) request;
    String header = req.getHeader(LOGIN_SIGN);
    JWTToken JWTToken = new JWTToken(header);
    //    super.getSubject(request, response).login(JWTToken);
    return true;
  }

  @SneakyThrows
  @Override
  protected boolean isAccessAllowed(
      ServletRequest request, ServletResponse response, Object mappedValue) {
    if (isLoginAttempt(request, response)) {
      try {
        if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
          return true;
        }
        executeLogin(request, response);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    } else {
      return false;
    }
    return true;
  }

  /** 对跨域提供支持 */
  @Override
  protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    //    httpServletResponse.setHeader(
    //        "Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
    httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST,PUT,GET,OPTIONS,DELETE");
    httpServletResponse.setHeader(
        "Access-Control-Allow-Headers",
        httpServletRequest.getHeader("Access-Control-Request-Headers"));
    httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
    httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
    httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
    log.info("filter preHandler {}", ((HttpServletRequest) request).getRequestURI());
    // 给option请求直接返回正常状态
    if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
      httpServletResponse.setStatus(HttpStatus.OK.value());
      return true;
    }
    return super.preHandle(request, response);
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    HttpServletResponse httpResp = WebUtils.toHttp(response);
    HttpServletRequest httpReq = WebUtils.toHttp(request);

    //    httpResp.addHeader("Access-Control-Allow-Origin", httpReq.getHeader("Origin"));
    httpResp.addHeader("Access-Control-Allow-Headers", "*");
    httpResp.addHeader("Access-Control-Allow-Methods", "*");
    httpResp.addHeader("Access-Control-Allow-Credentials", "true");

    //        this.saveRequestAndRedirectToLogin(request, response);
    request.setAttribute("tokenOut", "令牌已过期");
    throw new TokenInvalidException("");
  }
}
