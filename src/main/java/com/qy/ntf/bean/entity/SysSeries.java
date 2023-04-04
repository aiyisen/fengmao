package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author 王振读 2022-07-15 19:26:43 DESC : 系列主体 实体
 */
@TableName("sys_series")
@Data
public class SysSeries extends BaseEntity {

  /** */
  @TableField(value = "id")
  private Long id;

  /** 发行方id */
  @TableField(value = "sysOrgId")
  private Long sysorgid;

  /** 系列名称 */
  @TableField(value = "seriesName")
  private String seriesname;

  /** 系列详情 */
  @TableField(value = "seriesDetail")
  private String seriesdetail;

  /** 开始时间 */
  @TableField(value = "startTime")
  private Date starttime;

  /** 截止时间 */
  @TableField(value = "endTime")
  private Date endtime;

  /** 背景颜色值 */
  @TableField(value = "bgColor")
  private String bgcolor;

  /** 首图地址 */
  @TableField(value = "indexImg")
  private String indeximg;
}
