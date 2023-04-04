package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 聚合池藏品订单 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "聚合池藏品订单 查询参数", description = "聚合池藏品订单 查询参数")
public class OrderTreasurePoolSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 订单指纹唯一标识 */
  @ApiModelProperty(value = "订单指纹唯一标识", example = "订单指纹唯一标识", required = false)
  private String orderFingerprint;

  /** 藏品id */
  @ApiModelProperty(value = "藏品id", example = "藏品id", required = false)
  private Long teaPoId;

  /** 藏品类型0首页藏品1聚合池 */
  @ApiModelProperty(value = "藏品类型0首页藏品1聚合池", example = "藏品类型0首页藏品1聚合池", required = false)
  private Integer itemType;

  /** 快照单价 */
  @ApiModelProperty(value = "快照单价", example = "快照单价", required = false)
  private BigDecimal curPrice;

  /** 总价 */
  @ApiModelProperty(value = "总价", example = "总价", required = false)
  private BigDecimal totalPrice;

  /** 订单状态：-1已取消0待付款1发放中2已完成 */
  @ApiModelProperty(
      value = "订单状态：-1已取消0待付款1发放中2已完成",
      example = "订单状态：-1已取消0待付款1发放中2已完成",
      required = false)
  private Integer orderFlag;

  /** 支付方式0微信1支付宝2applePay */
  @ApiModelProperty(
      value = "支付方式0微信1支付宝2applePay",
      example = "支付方式0微信1支付宝2applePay",
      required = false)
  private Integer payType;

  /** 支付截止时间 */
  @ApiModelProperty(value = "支付截止时间", example = "支付截止时间", required = false)
  private Date payEndTime;
}
