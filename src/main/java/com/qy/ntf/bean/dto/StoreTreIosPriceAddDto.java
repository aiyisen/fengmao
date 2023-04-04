package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 王振读 2022-07-31 20:17:27 DESC : ios价格 添加参数
 */
@Data
@ApiModel(value = "ios价格 添加参数", description = "ios价格 添加参数")
public class StoreTreIosPriceAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** ios价格标题 */
  @ApiModelProperty(value = "ios价格标题", example = "ios价格标题", required = true)
  @NotEmpty(message = " ios价格标题不可为空")
  private String priceTitle;

  /** ios价格内容 */
  @ApiModelProperty(value = "ios价格内容", example = "ios价格内容", required = true)
  @NotEmpty(message = " ios价格内容不可为空")
  private String priceContent;

  /** ios价格 */
  @ApiModelProperty(value = "ios价格", example = "ios价格", required = true)
  @NotNull(message = " 价格不可为空")
  @Min(value = 0, message = "价格最小为0")
  private BigDecimal price;
}
