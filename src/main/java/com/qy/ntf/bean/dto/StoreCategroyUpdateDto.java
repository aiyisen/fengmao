package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王振读 email Created on 2022-08-06 13:01:44 DESC : 藏品分类 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "藏品分类 修改参数", description = "藏品分类 修改参数")
public class StoreCategroyUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 分类名称 */
  @ApiModelProperty(value = "分类名称", example = "分类名称", required = false)
  private String cateName;
}
