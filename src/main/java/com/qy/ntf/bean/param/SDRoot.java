package com.qy.ntf.bean.param;

import lombok.Data;

/**
 * Auto-generated: 2022-09-28 20:3:57
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class SDRoot {

  private String charset;
  private String data;
  private String signType;
  private String sign;
  private String extend;
  private String midBackNoticeUrl;

  public void setData(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }

  public void setSignType(String signType) {
    this.signType = signType;
  }

  public String getSignType() {
    return signType;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getSign() {
    return sign;
  }

  public void setExtend(String extend) {
    this.extend = extend;
  }

  public String getExtend() {
    return extend;
  }

  public void setMidBackNoticeUrl(String midBackNoticeUrl) {
    this.midBackNoticeUrl = midBackNoticeUrl;
  }

  public String getMidBackNoticeUrl() {
    return midBackNoticeUrl;
  }
}
