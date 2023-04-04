package com.qy.ntf.bean.customResult;

import com.qy.ntf.bean.entity.OrderProductRecord;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleAfterOrder {
  private Long orderId;
  private Long proId;
  private OrderProductRecord storeProduct;
  private BigDecimal orderTotalPrice;
  private Integer productCount;

  private Integer afterType;
  private Integer afterState;
  private Integer orderState;
  private String userLog;
}
