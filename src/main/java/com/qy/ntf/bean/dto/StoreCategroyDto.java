package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-08-06 13:01:44 DESC : 藏品分类 添加参数
 */
@Data
@ApiModel(value = "藏品分类 添加参数", description = "藏品分类 添加参数")
public class StoreCategroyDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 分类名称 */
  @ApiModelProperty(value = "分类名称", example = "分类名称", required = true)
  private String cateName;
}
