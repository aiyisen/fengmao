package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统轮播 实体
 */
@TableName("sys_banner")
@Data
public class SysBanner extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 轮播图片路径 */
  private String bannerPath;

  /** 序号 */
  private Integer orderSum;

  private Integer bannerType;
  private String linkUrl;
}
