package com.qy.ntf.util.wxPay;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/** 微信支付通知 作者:WZD 2017年8月16日 下午4:16:06 */
@XmlRootElement(name = "xml")
public class WXPayResult {
  private String return_code;
  private String return_msg;
  /**
   * @return the return_code
   */
  @XmlElement
  public String getReturn_code() {
    return return_code;
  }
  /**
   * @param return_code the return_code to set
   */
  public void setReturn_code(String return_code) {
    this.return_code = return_code;
  }
  /**
   * @return the return_msg
   */
  @XmlElement
  public String getReturn_msg() {
    return return_msg;
  }
  /**
   * @param return_msg the return_msg to set
   */
  public void setReturn_msg(String return_msg) {
    this.return_msg = return_msg;
  }
  /** 作者:WZD 2017年8月16日 下午4:17:21 */
  @Override
  public String toString() {
    return "WXPayResult [return_code=" + return_code + ", return_msg=" + return_msg + "]";
  }
}
