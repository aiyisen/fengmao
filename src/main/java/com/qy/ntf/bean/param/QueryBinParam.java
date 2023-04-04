package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class QueryBinParam {
  private String oid_partner;
  private String sign;
  private String sign_type = "RSA";
  private String user_id;
  private String pay_type = "2";
  private Integer offset = 0;
}
