package com.qy.ntf.bean.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopItem {
  private Long userId;
  private String userName;
  private String headImg;
  private BigDecimal total;
}
