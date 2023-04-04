package com.qy.ntf.controller.common;

import com.qy.ntf.bean.customResult.NotInvi;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.MyException;
import com.qy.ntf.util.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wzd
 * @title: ExceptionHandler
 * @projectName
 * @description:
 * @date 2021/7/411:39
 */
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ApiResponse handlerRuntimeException(RuntimeException ex) {
    log.error("拦截到运行异常" + ex.getMessage());
    ex.printStackTrace();
    return ApiResponse.fail(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ApiResponse handlerException(Exception ex) {
    log.error("拦截到普通异常" + ex.getMessage());
    ex.printStackTrace();
    return ApiResponse.fail(ex.getMessage());
  }

  @ExceptionHandler(MyException.class)
  public ApiResponse handlerMyException(Exception ex) {
    log.error("拦截到普通异常" + ex.getMessage());
    ex.printStackTrace();
    return ApiResponse.fail(ex.getMessage());
  }

  @ExceptionHandler(TokenInvalidException.class)
  public NotInvi handlerTokenInvalidException(
      TokenInvalidException ex, HttpServletRequest request, HttpServletResponse response) {
    log.error("拦截到令牌失效异常" + ex.getMessage());
    NotInvi res = new NotInvi();
    if (request.getAttribute("tokenOut") != null) {
      res.setCode(-2);
      res.setMsg("令牌已失效");
      return res;
    }
    return null;
  }
}
