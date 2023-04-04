package com.qy.ntf.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GivingParam {
  @ApiModelProperty("被赠送的用户Id")
  private String beGivingUserId;

  @ApiModelProperty("藏品订单指纹")
  private String orderFingerprint;

  @ApiModelProperty("藏品id,由我的藏品列表返回的id")
  private Long id;
  // type1赠送0出售
  private Integer type;
  private String pass;
  // 出售价格
  private BigDecimal price;
  private String tags;
}
