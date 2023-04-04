package com.qy.ntf.bean.param;

import lombok.Data;

@Data
public class BuyVipParam {
  private Integer payType;

  // 应用渠道标识。
  // 0， App-Android。
  // 1， App-iOS。
  // 2， Web。
  // 3， H5
  private String flagChnl;
  // 身份证号
  private String idNo;
  // 用户姓名，为用户在银行预留的姓名信息
  private String acctName;
  // 用户银行卡卡号。
  private String cardNo;
  // 签约协议编号 传递该参数后无需传递 id_no acct_name card_no 参数
  private String noAgree;
}
