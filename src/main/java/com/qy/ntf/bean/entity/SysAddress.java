package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 收货地址 实体
 */
@TableName("sys_address")
@Data
public class SysAddress extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户id */
  private Long uId;

  /** 省级/直辖市id */
  private String provName;

  /** 市级id */
  private String cityName;

  /** 县级id */
  private String conName;

  private Integer isDefault;

  /** 详细 */
  @TableField(value = "detail")
  private String detail;

  @TableField(value = "mobile")
  private String mobile;

  @TableField(value = "username")
  private String username;
}
