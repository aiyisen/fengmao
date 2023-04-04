package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-06-13 22:37:54 DESC : 权益商城售后订单 仅退款014 退货退款0234 退货024 添加参数
 */
@Data
@ApiModel(
    value = "权益商城售后订单 仅退款014 退货退款0234 退货024 添加参数",
    description = "权益商城售后订单 仅退款014 退货退款0234 退货024 添加参数")
public class OrderProAfterAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 权益商城订单id */
  @ApiModelProperty(value = "权益商城订单id", example = "权益商城订单id", required = true)
  private Long orderProductId;

  /** 0仅退款1退货退款2换货 */
  @ApiModelProperty(value = "0仅退款1退货退款2换货", example = "0仅退款1退货退款2换货", required = true)
  private Integer orderType;

  /** 当前售后订单状态0待平台审核1已退款2用户已发货3平台已发货4已完成本轮售后 */
  @ApiModelProperty(
      value = "当前售后订单状态0待平台审核1已退款2用户已发货3平台已发货4已完成本轮售后",
      example = "当前售后订单状态0待平台审核1已退款2用户已发货3平台已发货4已完成本轮售后",
      required = true)
  private Integer orderAfterState;

  /** 原因 */
  @ApiModelProperty(value = "原因", example = "原因", required = true)
  private String orderReason;

  /** 记录类型0用户发起1平台n+1发货2用户发货3平台退款4平台驳回退款 */
  @ApiModelProperty(
      value = "记录类型0用户发起1平台n+1发货2用户发货3平台退款4平台驳回退款",
      example = "记录类型0用户发起1平台n+1发货2用户发货3平台退款4平台驳回退款",
      required = true)
  private Integer recordType;

  /** 货物状态：0未收到货1已收到货 */
  @ApiModelProperty(value = "货物状态：0未收到货1已收到货", example = "货物状态：0未收到货1已收到货", required = true)
  private Integer orderProductState;

  /** 凭证图片，至多3张，“;"拼接 */
  @ApiModelProperty(value = "凭证图片，至多3张，';'拼接", example = "凭证图片，至多3张，';'拼接", required = true)
  private String voucherImgs;

  /** 平台发货物流单号 */
  @ApiModelProperty(value = "平台发货物流单号", example = "平台发货物流单号", required = true)
  private String plateTransNum;

  /** 用户退回物流单号 */
  @ApiModelProperty(value = "用户退回物流单号", example = "用户退回物流单号", required = true)
  private String userTransNum;

  /** 用户是否再次收到货0否1是 */
  @ApiModelProperty(value = "用户是否再次收到货0否1是", example = "用户是否再次收到货0否1是", required = true)
  private Integer userIsGet;
}
