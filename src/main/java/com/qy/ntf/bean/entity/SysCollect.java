package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author 王振读 2022-06-01 21:24:13 DESC : 实体
 */
@TableName("sys_collect")
@Data
public class SysCollect {

  /** */
  @TableField(value = "id")
  private Long id;

  /** */
  @TableField(value = "date")
  private Date date;

  /** */
  private Integer totalCount;
}
