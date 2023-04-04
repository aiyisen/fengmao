package com.qy.ntf.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author 王振读 2022-07-05 20:10:31 DESC : 发行方 添加参数
 */
@Data
@ApiModel(value = "发行方 添加参数", description = "发行方 添加参数")
public class SysOrgAddDto {

  /** 发行方名称 */
  @ApiModelProperty(value = "发行方名称", example = "发行方名称", required = true)
  @NotEmpty
  private String orgname;

  //  @NotEmpty(message = " 不可为空")
  private String orgInfo;
  /** 发行方头像地址 */
  @ApiModelProperty(value = "发行方头像地址", example = "发行方头像地址", required = true)
  @NotEmpty
  private String orgimg;
}
