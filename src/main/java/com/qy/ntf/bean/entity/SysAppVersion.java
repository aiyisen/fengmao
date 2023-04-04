package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 王振读 2022-07-22 00:02:04 DESC : 实体
 */
@TableName("sys_app_version")
@Data
public class SysAppVersion {

  /** */
  @TableField(value = "id")
  private Long id;

  /** 0Android1IOS */
  @TableField(value = "cType")
  private Integer ctype;

  /** 版本号 */
  @TableField(value = "version")
  private String version;

  /** 更新内容 */
  @TableField(value = "content")
  private String content;

  /** 下载地址 */
  @TableField(value = "url")
  private String url;
}
