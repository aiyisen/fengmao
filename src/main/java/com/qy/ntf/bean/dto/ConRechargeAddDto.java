package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 充值订单表 添加参数
 */
@Data
@ApiModel(value = "充值订单表 添加参数", description = "充值订单表 添加参数")
public class ConRechargeAddDto extends BaseEntity {

  /** */

  /** 充值订单状态0待付款1已付款 */
  @ApiModelProperty(
      value = "支付方式0微信1支付宝2applePay3余额支付",
      example = "充值订单状态0待付款1已付款",
      required = true)
  private Integer payType;

  /** 充值金额 */
  @ApiModelProperty(value = "充值金额", example = "充值金额", required = true)
  private BigDecimal totalPrice;

  // 应用渠道标识。
  // 0， App-Android。
  // 1， App-iOS。
  // 2， Web。
  // 3， H5
  private String flagChnl;
  // 身份证号
  private String idNo;
  // 用户姓名，为用户在银行预留的姓名信息
  private String acctName;
  // 用户银行卡卡号。
  private String cardNo;
  // 签约协议编号 传递该参数后无需传递 id_no acct_name card_no 参数
  private String noAgree;
}
