package com.qy.ntf.bean.customResult;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductOrderPrice {
  private BigDecimal itemPrice;
  private BigDecimal freight;
  private Integer count;

  private BigDecimal realTotalPrice;
  private BigDecimal totalPrice;
}
