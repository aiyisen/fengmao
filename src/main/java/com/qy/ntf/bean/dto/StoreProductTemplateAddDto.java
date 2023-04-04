package com.qy.ntf.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 特权商品材料模板 添加参数
 */
@Data
@ApiModel(value = "特权商品材料模板 添加参数", description = "特权商品材料模板 添加参数")
public class StoreProductTemplateAddDto {

  /** 所需商品id */
  @ApiModelProperty(value = "所需商品id", example = "所需商品id", required = true)
  private Long needId;

  @ApiModelProperty(value = "所需数量")
  @Min(value = 0, message = "数量应>0")
  private Integer needCount;
}
