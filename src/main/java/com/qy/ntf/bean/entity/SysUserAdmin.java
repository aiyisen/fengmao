package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-31 22:14:55 DESC : 管理员表 实体
 */
@TableName("sys_user_admin")
@Data
public class SysUserAdmin extends BaseEntity {

  /** */
  @TableField(value = "id")
  private Long id;

  /** 管理员名称 */
  private String adminName;

  /** 管理员手机号 */
  private String adminMobile;

  /** 管理员账号 */
  private String adminAccount;

  /** 管理员密码 */
  private String adminPass;
}
