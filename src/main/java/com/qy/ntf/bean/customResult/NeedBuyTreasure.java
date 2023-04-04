package com.qy.ntf.bean.customResult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NeedBuyTreasure {
  @ApiModelProperty("藏品首图")
  private String headImgPath;

  @ApiModelProperty("藏品id")
  private Long teaPoId;

  @ApiModelProperty("需要数量")
  private Integer count;

  @ApiModelProperty("藏品标题")
  private String treasureTitle;

  @ApiModelProperty("是否已拥有")
  private Boolean isBuy;

  private Integer tType;
}
