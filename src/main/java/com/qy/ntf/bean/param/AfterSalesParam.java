package com.qy.ntf.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AfterSalesParam {
  @ApiModelProperty("订单id")
  private Long orderId;

  @ApiModelProperty(value = "0仅退款1退货退款2换货")
  private Integer type;

  @ApiModelProperty(value = "物流单号")
  private String logisticsOrder;

  @ApiModelProperty(value = "物流名称")
  private String logisticsOrgName;

  @ApiModelProperty(value = "货物状态：0未收到货1已收到货", example = "货物状态：0未收到货1已收到货", required = true)
  private Integer orderProductState;

  @ApiModelProperty(value = "凭证图片，至多3张，';'拼接", example = "凭证图片，至多3张，';'拼接", required = true)
  private String voucherImgs;

  /** 原因 */
  @ApiModelProperty(value = "选择原因", example = "原因", required = true)
  private String orderReason;

  @ApiModelProperty(value = "说明", example = "说明", required = true)
  private String remark;

  @ApiModelProperty(value = "用户是否已收到货", example = "说明", required = true)
  private Integer userIsGet;
}
