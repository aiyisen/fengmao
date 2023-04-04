package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-08-15 15:18:56 DESC : 添加参数
 */
@Data
@ApiModel(value = " 添加参数", description = " 添加参数")
public class SysDropAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long uid;

  /** 空投类型：0按持有空投1拉新用户排名空投2指定用户空投 */
  @ApiModelProperty(
      value = "空投类型：0按持有空投1拉新用户排名空投2指定用户空投",
      example = "空投类型：0按持有空投1拉新用户排名空投2指定用户空投",
      required = true)
  private Integer droptype;

  /** 空投积分总值 */
  @ApiModelProperty(value = "空投积分总值", example = "空投积分总值", required = true)
  private Long dropintegralcount;

  /** 空投藏品id */
  @ApiModelProperty(value = "空投藏品id", example = "空投藏品id", required = true)
  private Long dropstreaid;
}
