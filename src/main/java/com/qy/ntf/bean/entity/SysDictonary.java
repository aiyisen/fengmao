package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-27 21:29:38 DESC : 实体
 */
@TableName("sys_dictonary")
@Data
public class SysDictonary extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 别名 */
  @TableField(value = "alias")
  private String alias;

  @TableField(value = "dic_title")
  private String dicTitle;

  /** 阈值 */
  @TableField(value = "threshold")
  private String threshold;
}
