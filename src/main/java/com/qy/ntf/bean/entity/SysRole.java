package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统角色 实体
 */
@TableName("sys_role")
@Data
public class SysRole extends BaseEntity {

  /** 主键 */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /** 角色名称 */
  @TableField(value = "name")
  private String name;

  /** 角色描述 */
  @TableField(value = "description")
  private String description;

  /** 排序号 */
  private Integer sortNumber;
}
