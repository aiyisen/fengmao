package com.qy.ntf.bean.customResult;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TreaTrading {
  private Long sTreasureId;
  private String title;
  private Integer type;
  private String imgPath;
  private BigDecimal price;
  private String beforeUser;
  private String afterUser;
}
