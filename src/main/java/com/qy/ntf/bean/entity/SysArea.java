package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 全国区域信息表 实体
 */
@TableName("sys_area")
@Data
public class SysArea {

  /** */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /** */
  private String areaName;

  /** */
  @TableField(value = "pid")
  private Integer pid;
}
