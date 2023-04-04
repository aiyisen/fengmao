package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 用户角色表 实体
 */
@TableName("sys_user_role")
@Data
public class SysUserRole {

  /** 用户编号 */
  private Long userId;

  /** 角色编号 */
  private Long roleId;
}
