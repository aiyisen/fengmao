package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 角色-菜单 实体
 */
@TableName("sys_role_menu")
@Data
public class SysRoleMenu extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /** */
  private Integer roleId;

  /** */
  private Integer menuId;
}
