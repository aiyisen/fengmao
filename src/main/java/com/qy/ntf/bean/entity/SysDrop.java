package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-08-15 15:18:56 DESC : 实体
 */
@TableName("sys_drop")
@Data
public class SysDrop extends BaseEntity {

  /** */
  @TableField(value = "id")
  private Long id;

  /** */
  @TableField(value = "uId")
  private Long uid;

  /** 空投类型：0按持有空投1拉新用户排名空投2指定用户空投 */
  @TableField(value = "dropType")
  private Integer droptype;

  /** 空投积分总值 */
  @TableField(value = "dropIntegralCount")
  private Long dropintegralcount;

  /** 空投藏品id */
  @TableField(value = "dropStreaId")
  private Long dropstreaid;
}
