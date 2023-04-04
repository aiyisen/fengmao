package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 充值订单表 实体
 */
@TableName("con_recharge")
@Data
public class ConRecharge extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户id */
  private Long uId;

  /** 订单指纹唯一标识 */
  private String orderFingerprint;

  private String tradeNo;

  private Integer payType;

  /** 充值订单状态0待付款1已付款 */
  private Integer orderFlag;

  /** 充值金额 */
  private BigDecimal totalPrice;
}
