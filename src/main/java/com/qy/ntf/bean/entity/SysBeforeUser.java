package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 余额记录 实体
 */
@TableName("sys_before_user")
@Data
public class SysBeforeUser extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  private Long streaId;
  private Integer ruleCount;

  private String phone;
}
