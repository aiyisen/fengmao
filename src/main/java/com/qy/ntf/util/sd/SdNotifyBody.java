package com.qy.ntf.util.sd;

import lombok.Data;

@Data
public class SdNotifyBody {
  private String mid;
  private String orderCode;
  private String tradeNo;
  private String clearDate;
  private String totalAmount;
  private String orderStatus;
  private String payTime;
  private String settleAmount;
  private String buyerPayAmount;
  private String discAmount;
  private String txnCompleteTime;
  private String payOrderCode;
  private String accLogonNo;
  private String accNo;
  private String midFee;
  private String extraFee;
  private String specialFee;
  private String plMidFee;
  private String bankserial;
  private String externalProductCode;
  private String cardNo;
  private String creditFlag;
  private String bid;
  private String fundBillList;
  private String benefitAmount;
  private String remittanceCode;
  private String extend;
}
