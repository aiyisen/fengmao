package com.qy.ntf.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 藏品首发/超前申购 主体 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "藏品首发/超前申购 主体 查询参数", description = "藏品首发/超前申购 主体 查询参数")
public class StoreTreasureSelectDto {

  /** 藏品类型0藏品首发1超前申购 */
  @ApiModelProperty(value = "藏品类型0藏品首发1超前申购")
  private Integer tType;

  @ApiModelProperty(value = "藏品标题", example = "藏品标题", required = true)
  private String treasureTitle;

  private Long sysSeriesId;
  private Long enCollectId;
}
