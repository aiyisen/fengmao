package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 王振读 email Created on 2022-06-01 21:24:13 DESC : 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = " 查询参数", description = " 查询参数")
public class SysCollectSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Date date;

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Integer totalCount;
}
