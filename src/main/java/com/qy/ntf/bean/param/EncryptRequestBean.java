package com.qy.ntf.bean.param;

/**
 * uber加密请求
 *
 * @author lihp
 * @date 2016-8-3 上午10:09:56
 */
public class EncryptRequestBean extends BaseRequestBean {

  /** */
  private static final long serialVersionUID = -7331592854807905125L;

  /** 商户编号 . */
  private String oid_partner;

  /** 加密串 ,使用连连加密工具包生成的加密串 */
  private String pay_load;

  public String getOid_partner() {
    return oid_partner;
  }

  public void setOid_partner(String oid_partner) {
    this.oid_partner = oid_partner;
  }

  public String getPay_load() {
    return pay_load;
  }

  public void setPay_load(String pay_load) {
    this.pay_load = pay_load;
  }
}
