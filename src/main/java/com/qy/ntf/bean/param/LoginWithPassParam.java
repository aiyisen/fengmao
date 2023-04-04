package com.qy.ntf.bean.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.bean.param @ClassName: LoginParam @Author:
 * 王振读 @Description: ${description} @Date: 2021/12/8 13:17 @Version: 1.0
 */
@Data
public class LoginWithPassParam {
  @NotNull(message = " 不可为空")
  private String phone;

  private String verifyCode;
  private String inviteCode;

  @NotNull(message = " 不可为空")
  private String pass;
}
