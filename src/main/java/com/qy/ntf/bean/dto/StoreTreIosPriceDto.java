package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-07-31 20:17:27 DESC : ios价格 添加参数
 */
@Data
@ApiModel(value = "ios价格 添加参数", description = "ios价格 添加参数")
public class StoreTreIosPriceDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** ios价格标题 */
  @ApiModelProperty(value = "ios价格标题", example = "ios价格标题", required = true)
  private String priceTitle;

  /** ios价格内容 */
  @ApiModelProperty(value = "ios价格内容", example = "ios价格内容", required = true)
  private String priceContent;

  /** ios价格 */
  @ApiModelProperty(value = "ios价格", example = "ios价格", required = true)
  private BigDecimal price;
}
