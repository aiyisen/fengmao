package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-05-27 21:29:38 DESC : 添加参数
 */
@Data
@ApiModel(value = " 添加参数", description = " 添加参数")
public class SysDictonaryDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 别名 */
  @ApiModelProperty(value = "别名", example = "别名", required = true)
  private String alias;

  private String dicTitle;
  /** 阈值 */
  @ApiModelProperty(value = "阈值", example = "阈值", required = true)
  private String threshold;
}
