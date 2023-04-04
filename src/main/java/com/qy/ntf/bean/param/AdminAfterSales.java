package com.qy.ntf.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdminAfterSales {
  @ApiModelProperty("订单id")
  private Long orderProductId;

  @ApiModelProperty("操作类型：-1退款审核驳回1退款审核通过5退货退款(换货）审核同意-5退货退款(换货）审核驳回 1平台退款（确认退货退款且已收货拨款）3平台发货（换货）")
  private Integer doType;

  @ApiModelProperty(value = "物流单号")
  private String logisticsOrder;

  @ApiModelProperty(value = "物流名称")
  private String logisticsOrgName;
  /** 原因 */
  @ApiModelProperty(value = "选择原因", example = "原因", required = true)
  private String orderReason;

  @ApiModelProperty(value = "0仅退款1退货退款2换货")
  private Integer type;
}
