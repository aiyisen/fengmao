package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 全国区域信息表 添加参数
 */
@Data
@ApiModel(value = "全国区域信息表 添加参数", description = "全国区域信息表 添加参数")
public class SysAreaDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private String areaName;

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Integer pid;
}
