package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 实物商品订单 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "实物商品订单 修改参数", description = "实物商品订单 修改参数")
public class OrderProductUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 订单指纹唯一标识 */
  @ApiModelProperty(value = "订单指纹唯一标识", example = "订单指纹唯一标识", required = false)
  private String orderFingerprint;

  /** 用户id、 */
  @ApiModelProperty(value = "用户id、", example = "用户id、", required = false)
  private Long uId;

  /** 总价 */
  @ApiModelProperty(value = "总价", example = "总价", required = false)
  private BigDecimal totalPrice;

  /** 订单中商品数量 */
  @ApiModelProperty(value = "订单中商品数量", example = "订单中商品数量", required = false)
  private Integer proCount;

  /** 商品单价 */
  @ApiModelProperty(value = "商品单价", example = "商品单价", required = false)
  private BigDecimal proPrice;

  /** 收货地址id */
  @ApiModelProperty(value = "收货地址id", example = "收货地址id", required = false)
  private Long sysAddressId;

  /** 订单状态：-1已取消0待付款1代发货2待收货3已完成 */
  @ApiModelProperty(
      value = "订单状态：-1已取消0待付款1代发货2待收货3已完成",
      example = "订单状态：-1已取消0待付款1代发货2待收货3已完成",
      required = false)
  private Integer orderFlag;

  /** 运费 */
  @ApiModelProperty(value = "运费", example = "运费", required = false)
  private BigDecimal freight;

  /** 支付方式0微信1支付宝2applePay */
  @ApiModelProperty(
      value = "支付方式0微信1支付宝2applePay",
      example = "支付方式0微信1支付宝2applePay",
      required = false)
  private Integer payType;

  /** 支付截止时间 */
  @ApiModelProperty(value = "支付截止时间", example = "支付截止时间", required = false)
  private Date payEndTime;

  /** 物流单号 */
  @ApiModelProperty(value = "物流单号", example = "物流单号", required = false)
  private String logisticsOrder;

  /** 订单备注 */
  @ApiModelProperty(value = "订单备注", example = "订单备注", required = false)
  private String orderRemark;
}
