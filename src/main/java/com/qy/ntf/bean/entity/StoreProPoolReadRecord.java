package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-28 00:02:44 DESC : 实体
 */
@TableName("store_pro_pool_read_record")
@Data
public class StoreProPoolReadRecord extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 聚合池id */
  private Long storeProPoolId;

  /** 用户id */
  private Long uId;
}
