package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class BindThirdParam {
  private String mobile;
  private String openId;
  private Integer type; // 0苹果1微信2qq
}
