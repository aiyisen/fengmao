package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-05-28 15:09:30 DESC : 添加参数
 */
@Data
@ApiModel(value = " 添加参数", description = " 添加参数")
public class StoreTreasureNeededDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 被合成藏品id */
  @ApiModelProperty(value = "被合成藏品id", example = "被合成藏品id", required = true)
  private Long storeTreasureId;

  /** 所需藏品id */
  @ApiModelProperty(value = "所需藏品id", example = "所需藏品id", required = true)
  private Long needStoreTreasureId;

  /** 所需数量 */
  @ApiModelProperty(value = "所需数量", example = "所需数量", required = true)
  private Integer needCount;
}
