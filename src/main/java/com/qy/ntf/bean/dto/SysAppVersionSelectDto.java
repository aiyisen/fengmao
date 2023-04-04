package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王振读 email Created on 2022-07-22 00:02:04 DESC : 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = " 查询参数", description = " 查询参数")
public class SysAppVersionSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 0Android1IOS */
  @ApiModelProperty(value = "0Android1IOS", example = "0Android1IOS", required = false)
  private Integer ctype;

  /** 版本号 */
  @ApiModelProperty(value = "版本号", example = "版本号", required = false)
  private String version;

  /** 更新内容 */
  @ApiModelProperty(value = "更新内容", example = "更新内容", required = false)
  private String content;

  /** 下载地址 */
  @ApiModelProperty(value = "下载地址", example = "下载地址", required = false)
  private String url;
}
