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
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池藏品订单 添加参数
 */
@Data
@ApiModel(value = "聚合池藏品订单 添加参数", description = "聚合池藏品订单 添加参数")
public class OrderTreasurePoolDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 订单指纹唯一标识 */
  @ApiModelProperty(value = "订单指纹唯一标识", example = "订单指纹唯一标识", required = true)
  private String orderFingerprint;

  /** 藏品id */
  @ApiModelProperty(value = "藏品id", example = "藏品id", required = true)
  private Long teaPoId;

  /** 藏品类型0首页藏品1聚合池 */
  @ApiModelProperty(value = "藏品类型0首页藏品1聚合池", example = "藏品类型0首页藏品1聚合池", required = true)
  private Integer itemType;

  /** 快照单价 */
  @ApiModelProperty(value = "快照单价", example = "快照单价", required = true)
  private BigDecimal curPrice;

  /** 总价 */
  @ApiModelProperty(value = "总价", example = "总价", required = true)
  private BigDecimal totalPrice;

  /** 订单状态：-1已取消0待付款1发放中2已完成 */
  @ApiModelProperty(
      value = "订单状态：-1已取消0待付款1出售中2已完成",
      example = "订单状态：-1已取消0待付款1出售中2已完成",
      required = true)
  private Integer orderFlag;

  /** 抽签序号 */
  @ApiModelProperty(value = "抽签序号", example = "总价", required = true)
  private String orderNum;
  /** 抽奖标识0未抽中1已抽中 */
  @ApiModelProperty(value = "抽奖标识0未抽中1已抽中", example = "总价", required = true)
  private Integer checkFlag;

  /** 支付方式0微信1支付宝2applePay */
  @ApiModelProperty(
      value = "支付方式0微信1支付宝2applePay",
      example = "支付方式0微信1支付宝2applePay",
      required = true)
  private Integer payType;

  /** 支付截止时间 */
  @ApiModelProperty(value = "支付截止时间", example = "支付截止时间", required = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date payEndTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date payTime;

  @ApiModelProperty("是否正在出售：0否1是")
  private Integer isSaleIng;

  @ApiModelProperty(value = "聚合池，藏品 订单商品快照")
  private OrderTreasureRecordDto orderTreasureRecord;

  private String creatorPhone;
}
