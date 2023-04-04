package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品订单 实体
 */
@TableName("order_product")
@Data
public class OrderProduct extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 订单指纹唯一标识 */
  private String orderFingerprint;

  /** 用户id、 */
  private Long uId;
  /** 商品id */
  private Long productId;

  private Integer productType;
  /** 总价 */
  private BigDecimal totalPrice;

  /** 订单中商品数量 */
  private Integer proCount;

  /** 商品单价 */
  private BigDecimal proPrice;

  /** 收货地址id */
  private Long sysAddressId;

  private String tradeNo;
  /** 订单状态：-1已取消0待付款1代发货2待收货3已完成 */
  private Integer orderFlag;

  /** 运费 */
  private BigDecimal freight;

  /** 支付方式0微信1支付宝2applePay */
  private Integer payType;

  /** 支付截止时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date payEndTime;

  /** 物流单号 */
  private String logisticsOrder;

  /** 订单备注 */
  private String orderRemark;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date payTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date sendTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date saveTime;

  private Integer saleAfterActive;

  private String backLogisticsOrder;

  private String secLogisticsOrder;
}
