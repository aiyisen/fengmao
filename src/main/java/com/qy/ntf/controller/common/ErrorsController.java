package com.qy.ntf.controller.common;

import com.qy.ntf.bean.customResult.NotInvi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ApiIgnore
@RestController
@Slf4j
public class ErrorsController
    implements org.springframework.boot.web.servlet.error.ErrorController {
  //    public ErrorController() {
  //        super(new DefaultErrorAttributes(), new ErrorProperties());
  //    }
  //
  //    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  //    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request,
  // HttpServletResponse response) {
  //        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request,
  // MediaType.ALL));
  //        HttpStatus status = getStatus(request);
  //        log.error("controller捕捉到异常...{}"+request.getRequestURI(), body.get("message"));
  //        String msg = (String) body.get("message");
  //        if(msg.indexOf("Authentication failed for token submission") != -1){
  //            ApiResponse<Object> res = new ApiResponse<>(401, "登录失效", null);
  //            return new ResponseEntity(res,HttpStatus.UNAUTHORIZED);
  //        }
  //        return new ResponseEntity(ApiResponse.fail((String) body.get("message")), status);
  //    }
  //
  //    @Override
  //    public String getErrorPath() {
  //        return "/error";
  //    }
  @Override
  public String getErrorPath() {
    return "/error";
  }

  @RequestMapping("/error")
  public NotInvi handleError(HttpServletRequest request, HttpServletResponse response)
      throws Throwable {
    response.setHeader("Content-Type", "application/json;charset=UTF-8");
    NotInvi res = new NotInvi();
    response.setStatus(200);
    if (request.getAttribute("tokenOut") != null
        && request.getAttribute("tokenOut").equals("needToken")) {
      res.setCode(-2);
      res.setMsg("缺失令牌");
      return res;
    }
    if (request.getAttribute("tokenOut") != null
        && request.getAttribute("tokenOut").equals("invalidToken")) {
      res.setCode(-2);
      res.setMsg("令牌已失效");
      return res;
    } else if (request.getAttribute("tokenOut") != null) {
      res.setCode(-2);
      res.setMsg("令牌已失效");
      return res;
    }
    res.setCode(-1);
    res.setMsg("服务器异常：" + request.getRequestURI());
    return res;
  }
}
