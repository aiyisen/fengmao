package com.qy.ntf.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.bean.param @ClassName: LoginParam @Author:
 * 王振读 @Description: ${description} @Date: 2021/12/8 13:17 @Version: 1.0
 */
@Data
public class LoginParam {
  @ApiModelProperty(value = "用户名称")
  private String adminAccount;

  @ApiModelProperty(value = "用户密码")
  private String password;
}
