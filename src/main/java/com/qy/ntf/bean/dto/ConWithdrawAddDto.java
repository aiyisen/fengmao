package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 提现订单表 添加参数
 */
@Data
@ApiModel(value = "提现订单表 添加参数", description = "提现订单表 添加参数")
public class ConWithdrawAddDto extends BaseEntity {

  /** 提现总额 */
  @ApiModelProperty(value = "提现总额", example = "提现总额", required = true)
  private BigDecimal withdrawTotal;

  private String aliPayIdentity;
  private String aliPayName;

  private String password;
  private String randomKey;
}
