package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class LLOrderBackNotifyParam {

  private String oid_partner;
  private String sign_type;
  private String sign;
  private String no_refund;
  private String dt_refund;
  private String money_refund;
  private String oid_refundno;
  private String sta_refund;
  private String settle_date;
}
