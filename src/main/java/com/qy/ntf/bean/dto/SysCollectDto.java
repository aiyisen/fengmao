package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 王振读 2022-06-01 21:24:13 DESC : 添加参数
 */
@Data
@ApiModel(value = " 添加参数", description = " 添加参数")
public class SysCollectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Date date;

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Integer totalCount;
}
