package com.qy.ntf.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author 王振读 email Created on 2022-07-05 20:10:31 DESC : 发行方 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "发行方 修改参数", description = "发行方 修改参数")
public class SysOrgUpdateDto {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  @NotNull
  private Long id;

  /** 发行方名称 */
  @ApiModelProperty(value = "发行方名称", example = "发行方名称", required = false)
  @NotEmpty
  private String orgname;

  //  @NotEmpty(message = " 不可为空")
  private String orgInfo;
  /** 发行方头像地址 */
  @ApiModelProperty(value = "发行方头像地址", example = "发行方头像地址", required = false)
  @NotEmpty
  private String orgimg;
}
