package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class LLCardBinParam {
  private String user_id;
  private String oid_partner;
  private String sign_type = "RSA";
  private String sign;
  private String notify_url;
  private String no_order;
  private String dt_order;
  private String pay_type = "2";
  private String risk_item;
  private String card_no;
  private String acct_name;
  private String bind_mob;
  private String id_type = "0";
  private String id_no;

  @Data
  public static class RiskItem {
    /** 业务来源。 0 - 商户代扣模式 10 - APP。 13 - PC。 16 - H5。 18 - IVR。 */
    private String frms_client_chnl;
    /** 用户交易请求IP，frms_client_chnl=0可不传 */
    private String frms_ip_addr;
    /** 用户授权标记。 0 - 否 1 - 是 该字段指开通API的商户，用户有无勾选连连支付服务协议。（商户接入微信扫码支付时，该字段无需传输） */
    private String user_auth_flag;
    /*用户真实姓名。*/
    private String user_info_full_name;
    private String user_info_id_typ = "0";
    /*身份証號*/
    private String user_info_id_no;
    private String user_info_identify_state = "1";
    private String user_info_identify_type = "3";
  }
}
