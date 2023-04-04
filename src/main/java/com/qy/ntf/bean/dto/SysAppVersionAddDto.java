package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-07-22 00:02:04 DESC : 添加参数
 */
@Data
@ApiModel(value = " 添加参数", description = " 添加参数")
public class SysAppVersionAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 0Android1IOS */
  @ApiModelProperty(value = "0Android1IOS", example = "0Android1IOS", required = true)
  private Integer ctype;

  /** 版本号 */
  @ApiModelProperty(value = "版本号", example = "版本号", required = true)
  private String version;

  /** 更新内容 */
  @ApiModelProperty(value = "更新内容", example = "更新内容", required = true)
  private String content;

  /** 下载地址 */
  @ApiModelProperty(value = "下载地址", example = "下载地址", required = true)
  private String url;
}
