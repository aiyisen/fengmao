package com.qy.ntf.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品订单 添加参数
 */
@Data
@ApiModel(value = "实物商品订单 添加参数", description = "实物商品订单 添加参数")
public class OrderProductAddDto {
  @ApiModelProperty(value = "商品id", required = true)
  private Long productId;
  /** 总价 */
  @ApiModelProperty(value = "当前页面总价", example = "总价", required = true)
  private BigDecimal totalPrice;

  /** 订单中商品数量 */
  @ApiModelProperty(value = "订单中商品数量", example = "订单中商品数量", required = true)
  private Integer proCount;

  /** 商品单价 */
  @ApiModelProperty(value = "商品单价", example = "商品单价", required = true)
  private BigDecimal proPrice;

  /** 收货地址id */
  @ApiModelProperty(value = "收货地址id", example = "收货地址id", required = true)
  private Long sysAddressId;

  /** 运费 */
  @ApiModelProperty(value = "当前页面运费", example = "运费", required = true)
  private BigDecimal freight;

  /** 支付方式0微信1支付宝2applePay */
  @ApiModelProperty(
      value = "支付方式0微信1支付宝2applePay3余额4连连pay",
      example = "支付方式0微信1支付宝2applePay3余额4连连pay")
  private Integer payType;

  /** 订单备注 */
  @ApiModelProperty(value = "订单备注", example = "订单备注", required = false)
  private String orderRemark;

  @ApiModelProperty(hidden = true)
  private Long userId;

  private BigDecimal vipPercent;

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
