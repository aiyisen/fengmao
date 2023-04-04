package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-07-05 20:10:31 DESC : 发行方 实体
 */
@TableName("sys_org")
@Data
public class SysOrg extends BaseEntity {

  /** */
  @TableField(value = "id")
  private Long id;

  /** 发行方名称 */
  @TableField(value = "orgName")
  private String orgname;

  @TableField(value = "orgInfo")
  private String orgInfo;

  /** 发行方头像地址 */
  @TableField(value = "orgImg")
  private String orgimg;
}
