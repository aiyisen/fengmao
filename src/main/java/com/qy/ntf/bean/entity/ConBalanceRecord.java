package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 余额记录 实体
 */
@TableName("con_balance_record")
@Data
public class ConBalanceRecord extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户id */
  private Long uId;

  /** 记录类型：0充值1提现2购买商品 */
  private Integer reType;

  /** 订单id */
  private String orderId;

  /** 订单类型0充值订单1提现申请3商品订单4藏品订单 */
  private Integer orderType;

  /** 总额 */
  private BigDecimal totalPrice;
  /** 交易渠道0微信1支付宝2applePay3余额支付 */
  private Integer tradingChannel;
}
