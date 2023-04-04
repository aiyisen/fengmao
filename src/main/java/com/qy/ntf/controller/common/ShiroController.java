package com.qy.ntf.controller.common;

import com.qy.ntf.util.ApiResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ShiroController {

  @GetMapping("test")
  public ApiResponse test() {
    return ApiResponse.success("测试无需认证");
  }

  @GetMapping("test1")
  @RequiresPermissions("shiro:all")
  public ApiResponse test1() {
    return ApiResponse.success("测试有权限");
  }

  @GetMapping("test2")
  @RequiresPermissions("shiro:xxx-xxx")
  public ApiResponse test2() {
    return ApiResponse.success("测试无权限");
  }
}
