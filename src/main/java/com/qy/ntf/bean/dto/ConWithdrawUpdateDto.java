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
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 提现订单表 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "提现订单表 修改参数", description = "提现订单表 修改参数")
public class ConWithdrawUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 提现总额 */
  @ApiModelProperty(value = "提现总额", example = "提现总额", required = false)
  private BigDecimal withdrawTotal;

  /** 提现状态0：待审核1审核通过2审核驳回 */
  @ApiModelProperty(value = "提现状态1审核通过2审核驳回", example = "提现状态1审核通过2审核驳回", required = false)
  private Integer applyFlag;

  /** 原因 */
  @ApiModelProperty(value = "原因", example = "原因", required = false)
  private String applyCause;
}
