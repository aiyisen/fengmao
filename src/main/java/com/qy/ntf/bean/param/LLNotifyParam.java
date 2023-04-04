package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class LLNotifyParam {
  private String oid_partner;
  private String sign_type;
  private String sign;
  private String dt_order;
  private String no_order;
  private String oid_paybill;
  private String money_order;
  private String result_pay;
  private String settle_date;
  private String info_order;
  private String pay_type;
  private String bank_code;
  private String no_agree;
  private String id_type;
  private String id_no;
  private String acct_name;
  private String card_no;
}
