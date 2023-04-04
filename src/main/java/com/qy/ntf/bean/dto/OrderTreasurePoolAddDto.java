package com.qy.ntf.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池藏品订单 添加参数
 */
@Data
@ApiModel(value = "聚合池藏品订单 添加参数", description = "聚合池藏品订单 添加参数")
public class OrderTreasurePoolAddDto {

  /** 藏品id */
  @ApiModelProperty(value = "藏品id", example = "藏品id", required = true)
  private Long teaPoId;

  private String pass;
  /** 藏品类型0首页藏品1聚合池 */
  @ApiModelProperty(value = "0首页藏品1超前申购2流转中心3盲盒", example = "0首页藏品1超前申购2聚合池", required = true)
  private Integer itemType;

  /** 快照单价 */
  @ApiModelProperty(value = "快照单价", example = "快照单价", required = true)
  private BigDecimal curPrice;

  @ApiModelProperty("购买数量")
  private Integer count = 1;

  /** 总价 */
  @ApiModelProperty(value = "总价", example = "总价", required = true)
  private BigDecimal totalPrice;

  /** 支付方式0微信1支付宝2applePay */
  @ApiModelProperty(
      value = "支付方式0微信1支付宝2applePay",
      example = "支付方式0微信1支付宝2applePay",
      required = false)
  private Integer payType;

  @ApiModelProperty(hidden = true)
  private Long userId;

  @ApiModelProperty(hidden = true)
  private String orderNum;

  @ApiModelProperty(hidden = true)
  private Integer isJoin;

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
