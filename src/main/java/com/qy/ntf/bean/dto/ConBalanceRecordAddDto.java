package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 余额记录 添加参数
 */
@Data
@ApiModel(value = "余额记录 添加参数", description = "余额记录 添加参数")
public class ConBalanceRecordAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = true)
  private Long uId;

  /** 记录类型：0充值1提现2购买商品 */
  @ApiModelProperty(value = "记录类型：0充值1提现2购买商品", example = "记录类型：0充值1提现2购买商品", required = true)
  private Integer reType;

  /** 订单id */
  @ApiModelProperty(value = "订单id", example = "订单id", required = true)
  private String orderId;

  /** 订单类型0充值订单1提现申请3商品订单4藏品订单 */
  @ApiModelProperty(
      value = "订单类型0充值订单1提现申请3商品订单4藏品订单",
      example = "订单类型0充值订单1提现申请3商品订单4藏品订单",
      required = true)
  private Integer orderType;

  /** 总额 */
  @ApiModelProperty(value = "总额", example = "总额", required = true)
  private BigDecimal totalPrice;
}
