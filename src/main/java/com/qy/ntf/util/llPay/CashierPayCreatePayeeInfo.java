package com.qy.ntf.util.llPay;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CashierPayCreatePayeeInfo {
  private String payee_id;
  private String payee_type;
  private String payee_accttype;
  private String payee_amount;
  private String payee_memo;
}
