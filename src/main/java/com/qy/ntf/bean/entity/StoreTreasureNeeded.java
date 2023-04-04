package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-28 15:09:30 DESC : 实体
 */
@TableName("store_treasure_needed")
@Data
public class StoreTreasureNeeded extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 被合成藏品id */
  private Long storeTreasureId;

  /** 所需藏品id */
  private Long needStoreTreasureId;

  /** 所需数量 */
  private Integer needCount;
}
