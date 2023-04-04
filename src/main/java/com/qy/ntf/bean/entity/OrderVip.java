package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-06-17 22:00:33 DESC : vip购买订单 实体
 */
@TableName("order_vip")
@Data
public class OrderVip extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** */
  private Long uId;

  /** 第三方流水 */
  private String tradeNo;

  /** 支付金额 */
  private BigDecimal payTotal;

  /** 0微信1支付宝2applePay3余额 */
  private Integer payType;

  /** 0待支付1已支付 */
  private Integer orderFlag;
}
