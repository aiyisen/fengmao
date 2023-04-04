package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 王振读 email Created on 2022-06-17 22:00:33 DESC : vip购买订单 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "vip购买订单 查询参数", description = "vip购买订单 查询参数")
public class OrderVipSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long uId;

  /** 第三方流水 */
  @ApiModelProperty(value = "第三方流水", example = "第三方流水", required = false)
  private String tradeNo;

  /** 支付金额 */
  @ApiModelProperty(value = "支付金额", example = "支付金额", required = false)
  private BigDecimal payTotal;

  /** 0微信1支付宝2applePay3余额 */
  @ApiModelProperty(
      value = "0微信1支付宝2applePay3余额",
      example = "0微信1支付宝2applePay3余额",
      required = false)
  private Integer payType;

  /** 0待支付1已支付 */
  @ApiModelProperty(value = "0待支付1已支付", example = "0待支付1已支付", required = false)
  private Integer orderFlag;
}
