package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 积分流水记录（记录总和>=0) 实体
 */
@TableName("con_integral_record")
@Data
public class ConIntegralRecord extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户id */
  private Long uId;

  /** 积分记录类型0登录赠送1邀请好友赠送2订单消费3刷一刷 */
  private Integer recordType;

  /** 涉及积分总量 */
  private Integer metaCount;

  private Long orderId;
}
