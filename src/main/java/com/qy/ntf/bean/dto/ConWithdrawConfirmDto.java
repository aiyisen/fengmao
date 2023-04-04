package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 提现订单表 添加参数
 */
@Data
@ApiModel(value = "提现订单表 添加参数", description = "提现订单表 添加参数")
public class ConWithdrawConfirmDto extends BaseEntity {

  /** 提现总额 */
  private String token;

  private String phone;
  private String verifyCode;
  private String txnSeqno;
}
