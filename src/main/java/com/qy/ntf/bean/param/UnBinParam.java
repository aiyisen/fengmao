package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class UnBinParam {
  private String oid_partner;
  private String sign;
  private String sign_type = "RSA";
  private String user_id;
  private String no_agree;
}
