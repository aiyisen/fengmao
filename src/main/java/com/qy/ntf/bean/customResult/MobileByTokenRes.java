package com.qy.ntf.bean.customResult;

import lombok.Data;

@Data
public class MobileByTokenRes {
  private String mobile;
  private Boolean isIn;
  private String token;
}
