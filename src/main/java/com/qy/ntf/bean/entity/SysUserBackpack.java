package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 用户背包（记录虚拟商品购买订单标识） 实体
 */
@TableName("sys_user_backpack")
@Data
public class SysUserBackpack extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户id */
  private Long uId;

  /** 藏品id */
  private Long sTreasureId;

  private Long orderTreasurePoolId;

  private Long beforeUserId;

  private Long afterUserId;

  private Integer treasureFrom;

  /** 订单指纹 */
  private String orderFingerprint;

  /** 记录类型：1赠送2获赠3空投4支出 */
  private Integer finType;

  private Integer isCheck;

  private String ddcId;
  private String ddcUrl;
  private String transationId;
}
