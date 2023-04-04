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
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 提现订单表 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "提现订单表 查询参数", description = "提现订单表 查询参数")
public class ConWithdrawSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = false)
  private Long uId;

  /** 订单指纹唯一标识 */
  @ApiModelProperty(value = "订单指纹唯一标识", example = "订单指纹唯一标识", required = false)
  private String orderFingerprint;

  /** 提现总额 */
  @ApiModelProperty(value = "提现总额", example = "提现总额", required = false)
  private BigDecimal withdrawTotal;

  /** 提现状态0：待审核1审核通过2审核驳回 */
  @ApiModelProperty(
      value = "提现状态0：待审核1审核通过2审核驳回",
      example = "提现状态0：待审核1审核通过2审核驳回",
      required = false)
  private Integer applyFlag;

  /** 拨付状态0未拨付1已拨付 */
  @ApiModelProperty(value = "拨付状态0未拨付1已拨付", example = "拨付状态0未拨付1已拨付", required = false)
  private Integer allocatedState;

  /** 原因 */
  @ApiModelProperty(value = "原因", example = "原因", required = false)
  private String applyCause;

  /** 审核时间 */
  @ApiModelProperty(value = "审核时间", example = "审核时间", required = false)
  private Date applyTime;
}
