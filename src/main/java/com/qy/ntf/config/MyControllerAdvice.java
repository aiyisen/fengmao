package com.qy.ntf.config;

import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.TokenInvalidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * controller 增强器
 *
 * @author sam
 * @since 2017/7/17
 */
@RestControllerAdvice
public class MyControllerAdvice {

  /**
   * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
   *
   * @param binder
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {}

  /**
   * 全局异常捕捉处理
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(value = Exception.class)
  public ApiResponse errorHandler(Exception ex, HttpServletResponse response) {
    ex.printStackTrace();
    String message = ex.getMessage();
    response.setHeader("Content-Type", "application/json;charset=UTF-8");
    if (ex instanceof TokenInvalidException) {
      return ApiResponse.fail("令牌失效", -2);
    } else if (message != null && message.equals("令牌失效")) {
      return ApiResponse.fail("令牌失效", -2);
    } else {
      return ApiResponse.fail(message);
    }
  }
  //    @ResponseBody
  //    @ExceptionHandler(value = AuthenticationException.class)
  //    public LingoResponse<Object> errorAuthentication(AuthenticationException ex) {
  //        return new LingoResponse<>(401,"登录失效",null);
  //    }

}
