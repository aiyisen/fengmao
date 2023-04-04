package com.qy.ntf.bean.param;

import com.qy.ntf.bean.dto.UserDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateOrderParam {
  @ApiModelProperty("订单id")
  private Long orderId;

  @ApiModelProperty("操作类型0取消订单1支付订单2收货/修改价格")
  private Integer type;

  @ApiModelProperty("支付方式0微信1支付宝2applePay3余额")
  private Integer payType;

  @ApiModelProperty(hidden = true)
  private UserDto userData;
  // ios 内购交易id
  private String transactionId;
  // ios 内购校验体 base64
  private String payload;

  @ApiModelProperty("修改后价格")
  private BigDecimal price;

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
