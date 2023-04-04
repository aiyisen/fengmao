package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王振读 email Created on 2022-05-28 15:09:30 DESC : 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = " 查询参数", description = " 查询参数")
public class StoreTreasureNeededSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 被合成藏品id */
  @ApiModelProperty(value = "被合成藏品id", example = "被合成藏品id", required = false)
  private Long storeTreasureId;

  /** 所需藏品id */
  @ApiModelProperty(value = "所需藏品id", example = "所需藏品id", required = false)
  private Long needStoreTreasureId;

  /** 所需数量 */
  @ApiModelProperty(value = "所需数量", example = "所需数量", required = false)
  private Integer needCount;
}
