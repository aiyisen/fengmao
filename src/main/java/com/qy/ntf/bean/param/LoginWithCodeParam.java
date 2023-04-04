package com.qy.ntf.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.bean.param @ClassName: LoginParam @Author:
 * 王振读 @Description: ${description} @Date: 2021/12/8 13:17 @Version: 1.0
 */
@Data
public class LoginWithCodeParam {
  @ApiModelProperty(value = "手机号", required = true)
  private String phone;

  @ApiModelProperty(value = "验证码", required = true)
  private String verifyCode;

  @ApiModelProperty(value = "邀请码")
  private String inviteCode;

  private String pass;
}
