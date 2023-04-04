package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 王振读 email Created on 2022-07-31 20:17:27 DESC : ios价格 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ios价格 修改参数", description = "ios价格 修改参数")
public class StoreTreIosPriceUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  @NotNull(message = " id不可为空")
  private Long id;

  /** ios价格标题 */
  @ApiModelProperty(value = "ios价格标题", example = "ios价格标题", required = false)
  @NotEmpty(message = " ios价格标题不可为空")
  private String priceTitle;

  /** ios价格内容 */
  @ApiModelProperty(value = "ios价格内容", example = "ios价格内容", required = false)
  @NotEmpty(message = " ios价格内容不可为空")
  private String priceContent;

  /** ios价格 */
  @ApiModelProperty(value = "ios价格", example = "ios价格", required = false)
  @NotNull(message = " 价格不可为空")
  @Min(value = 0, message = "价格最小为0")
  private BigDecimal price;
}
