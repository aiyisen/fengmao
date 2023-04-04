package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author 王振读 email Created on 2022-06-01 21:24:13 DESC : 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = " 修改参数", description = " 修改参数")
public class SysCollectUpdateDto extends BaseEntity {

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
