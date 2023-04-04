package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王振读 email Created on 2022-05-27 21:29:38 DESC : 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = " 修改参数", description = " 修改参数")
public class SysDictonaryUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 别名 */
  @ApiModelProperty(value = "别名", example = "别名", required = false)
  private String alias;

  /** 阈值 */
  @ApiModelProperty(value = "阈值", example = "阈值", required = false)
  private String threshold;

  private String dicTitle;
}
