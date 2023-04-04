package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-06-17 22:00:33 DESC : vip购买订单 添加参数
 */
@Data
@ApiModel(value = "vip购买订单 添加参数", description = "vip购买订单 添加参数")
public class OrderVipAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long uId;

  /** 第三方流水 */
  @ApiModelProperty(value = "第三方流水", example = "第三方流水", required = true)
  private String tradeNo;

  /** 支付金额 */
  @ApiModelProperty(value = "支付金额", example = "支付金额", required = true)
  private BigDecimal payTotal;

  /** 0微信1支付宝2applePay3余额 */
  @ApiModelProperty(value = "0微信1支付宝2applePay3余额", example = "0微信1支付宝2applePay3余额", required = true)
  private Integer payType;

  /** 0待支付1已支付 */
  @ApiModelProperty(value = "0待支付1已支付", example = "0待支付1已支付", required = true)
  private Integer orderFlag;
}
