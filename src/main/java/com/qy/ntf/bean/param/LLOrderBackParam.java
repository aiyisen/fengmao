package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class LLOrderBackParam {
  public String oid_partner;
  public String sign;
  public String sign_type;
  public String trader_refund;
  public String no_refund;
  public String dt_refund;
  public String money_refund;
  public String oid_paybill;
  public String notify_url;
}
