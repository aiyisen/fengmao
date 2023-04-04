package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-06-13 22:37:54 DESC : 权益商城售后订单 仅退款014 退货退款0234 退货024 实体
 */
@TableName("order_pro_after")
@Data
public class OrderProAfter extends BaseEntity {

  /** */
  @TableField(value = "id")
  private Long id;

  /** 权益商城订单id */
  private Long orderProductId;

  /** 0仅退款1退货退款2换货 */
  private Integer orderType;

  /** 当前售后订单状态0待平台审核1已退款2用户已发货3平台已发货4已完成本轮售后 */
  private Integer orderAfterState;
  /** 原因 */
  private String orderReason;

  //  /** 记录类型0用户发起1平台n+1发货2用户发货3平台退款4平台驳回退款 */
  //  @TableField(value = "record_type")
  //  private Integer recordType;

  /** 货物状态：0未收到货1已收到货 */
  private Integer orderProductState;

  /** 凭证图片，至多3张，“;"拼接 */
  private String voucherImgs;

  /** 平台发货物流单号 */
  private String plateTransNum;

  /** 用户退回物流单号 */
  private String userTransNum;

  private String remark;

  private String logisticsOrgName;

  /** 用户是否再次收到货0否1是 */
  private Integer userIsGet;
}
