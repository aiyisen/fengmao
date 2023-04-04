package com.qy.ntf.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 提现订单表 添加参数
 */
@Data
@ApiModel(value = "提现订单表 添加参数", description = "提现订单表 添加参数")
public class ConWithdrawDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = true)
  private Long uId;

  /** 订单指纹唯一标识 */
  @ApiModelProperty(value = "订单指纹唯一标识", example = "订单指纹唯一标识", required = true)
  private String orderFingerprint;

  /** 提现总额 */
  @ApiModelProperty(value = "提现总额", example = "提现总额", required = true)
  private BigDecimal withdrawTotal;

  private BigDecimal realPay;

  /** 提现状态0：待审核1审核通过2审核驳回 */
  @ApiModelProperty(value = "提现状态0：待审核1审核通过2审核驳回", example = "提现状态0：待审核1审核通过2审核驳回", required = true)
  private Integer applyFlag;

  /** 拨付状态0未拨付1已拨付 */
  @ApiModelProperty(value = "拨付状态0未拨付1已拨付", example = "拨付状态0未拨付1已拨付", required = true)
  private Integer allocatedState;

  /** 原因 */
  @ApiModelProperty(value = "原因", example = "原因", required = true)
  private String applyCause;

  private String aliPayIdentity;
  private String aliPayName;

  /** 审核时间 */
  @ApiModelProperty(value = "审核时间", example = "审核时间", required = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date applyTime;

  private String verifyCode;
}
