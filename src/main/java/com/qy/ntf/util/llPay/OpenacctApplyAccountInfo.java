package com.qy.ntf.util.llPay;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class OpenacctApplyAccountInfo {
  private String account_type;
  private String account_need_level;
}
