package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 王振读 email Created on 2022-07-31 20:17:27 DESC : ios价格 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ios价格 查询参数", description = "ios价格 查询参数")
public class StoreTreIosPriceSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** ios价格标题 */
  @ApiModelProperty(value = "ios价格标题", example = "ios价格标题", required = false)
  private String priceTitle;

  /** ios价格内容 */
  @ApiModelProperty(value = "ios价格内容", example = "ios价格内容", required = false)
  private String priceContent;

  /** ios价格 */
  @ApiModelProperty(value = "ios价格", example = "ios价格", required = false)
  private BigDecimal price;
}
