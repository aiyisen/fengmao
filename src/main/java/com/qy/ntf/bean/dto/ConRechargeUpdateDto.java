package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 充值订单表 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "充值订单表 修改参数", description = "充值订单表 修改参数")
public class ConRechargeUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = false)
  private Long uId;

  /** 订单指纹唯一标识 */
  @ApiModelProperty(value = "订单指纹唯一标识", example = "订单指纹唯一标识", required = false)
  private String orderFingerprint;

  /** 充值订单状态0待付款1已付款 */
  @ApiModelProperty(value = "充值订单状态0待付款1已付款", example = "充值订单状态0待付款1已付款", required = false)
  private Integer orderFlag;

  /** 充值金额 */
  @ApiModelProperty(value = "充值金额", example = "充值金额", required = false)
  private BigDecimal totalPrice;
}
