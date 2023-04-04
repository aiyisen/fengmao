package com.qy.ntf.bean.customResult;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderVipResult {
  private BigDecimal total;
  private List<OrderVipUserInfo> list;
}
