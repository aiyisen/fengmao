package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统权限 实体
 */
@TableName("sys_menu")
@Data
public class SysMenu extends BaseEntity {

  /** 主键 */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /** 上级主键 */
  @TableField(value = "pid")
  private Integer pid;

  /** 权限类型(1:目录 2:菜单 3:按钮) */
  @TableField(value = "type")
  private Long type;

  /** 权限名 */
  @TableField(value = "name")
  private String name;

  /** 权限标识 */
  @TableField(value = "identifying")
  private String identifying;

  /** 权限地址 */
  @TableField(value = "path")
  private String path;

  /** 权限图标 */
  @TableField(value = "icon")
  private String icon;

  /** 权限描述 */
  @TableField(value = "description")
  private String description;

  /** 创建人姓名 */
  private String createBy;

  /** 修改人姓名 */
  private String updateBy;

  /** 排序号 */
  private Long sortNumber;
}
