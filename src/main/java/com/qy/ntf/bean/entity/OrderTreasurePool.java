package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池藏品订单 实体
 */
@TableName("order_treasure_pool")
@Data
public class OrderTreasurePool extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  private String tradeNo;
  /** 订单指纹唯一标识 */
  private String orderFingerprint;

  /** 藏品id */
  private Long teaPoId;
  /** 藏品类型0首页藏品1聚合池3盲盒订单 */
  private Integer itemType;

  /** 快照单价 */
  private BigDecimal curPrice;

  /** 总价 */
  private BigDecimal totalPrice;

  /** 订单状态：-1已取消0待付款1发放中2已完成 */
  private Integer orderFlag;
  /** 抽签序号 */
  private String orderNum;
  /** 抽奖标识0未抽中1已抽中 */
  private Integer checkFlag;

  private Integer totalCount;

  /** 支付方式0微信1支付宝2applePay */
  private Integer payType;

  private Integer isJoin;

  /** 支付截止时间 */
  private Date payEndTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date payTime;
}
