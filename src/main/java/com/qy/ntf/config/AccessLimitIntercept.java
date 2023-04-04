package com.qy.ntf.config;

import cn.jiguang.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.NetworkUtil;
import org.apache.ibatis.annotations.Results;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitIntercept implements HandlerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(AccessLimitIntercept.class);

  @Autowired private StringRedisTemplate redisTemplate;

  /**
   * 接口调用前检查对方ip是否频繁调用接口
   *
   * @param request
   * @param response
   * @param handler
   * @return
   * @throws Exception
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    try {
      String authorization = request.getHeader("Authorization");
      if (authorization != null && !authorization.equals("")) {
        String userJson = redisTemplate.opsForValue().get(authorization);
        if (Strings.isEmpty(userJson)) {
          //          request.setAttribute("tokenOut", "无效的token");
          ApiResponse<Object> res = new ApiResponse<>(-2, "令牌失效", null);
          response.getOutputStream().write(JSONObject.toJSONString(res).getBytes());
          response.setHeader("Content-Type", "application/json;charset=UTF-8");
          response.setStatus(200);
          return false;
        }
      }
      // handler是否为 HandleMethod 实例
      if (handler instanceof HandlerMethod) {
        // 强转
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取方法
        Method method = handlerMethod.getMethod();
        // 判断方式是否有AccessLimit注解，有的才需要做限流
        if (!method.isAnnotationPresent(AccessLimit.class)) {
          return true;
        }

        // 获取注解上的内容
        AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
        if (accessLimit == null) {
          return true;
        }
        // 获取方法注解上的请求次数
        int times = accessLimit.times();
        // 获取方法注解上的请求时间
        int second = accessLimit.second();

        // 拼接redis key = IP + Api限流
        String key = NetworkUtil.getIpAddress(request) + request.getRequestURI();

        // 获取redis的value
        Integer maxTimes = null;
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(value)) {
          maxTimes = Integer.valueOf(value);
        }
        if (maxTimes == null) {
          // 如果redis中没有该ip对应的时间则表示第一次调用，保存key到redis
          redisTemplate.opsForValue().set(key, "1", second, TimeUnit.SECONDS);
        } else if (maxTimes < times) {
          // 如果redis中的时间比注解上的时间小则表示可以允许访问,这是修改redis的value时间
          redisTemplate.opsForValue().set(key, maxTimes + 1 + "", second, TimeUnit.SECONDS);
        } else {
          // 请求过于频繁
          logger.info(key + " 请求过于频繁");
          //          return setResponse(new Results(ResultEnum.BAD_REQUEST), response);
          throw new RuntimeException("超出访问流量控制,每" + second + "秒，允许访问：" + times + "次");
        }
      }
    } catch (Exception e) {
      logger.error("API请求限流拦截异常，异常原因：", e);
      throw new RuntimeException(e);
    }
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {}

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {}

  private boolean setResponse(Results results, HttpServletResponse response) throws IOException {
    ServletOutputStream outputStream = null;
    try {
      response.setHeader("Content-type", "application/json; charset=utf-8");
      outputStream = response.getOutputStream();
      outputStream.write(JSONObject.toJSONString(results).getBytes("UTF-8"));
    } catch (Exception e) {
      logger.error("setResponse方法报错", e);
      return false;
    } finally {
      if (outputStream != null) {
        outputStream.flush();
        outputStream.close();
      }
    }
    return true;
  }
}
