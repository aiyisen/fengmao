package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王振读 email Created on 2022-08-15 15:18:56 DESC : 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = " 查询参数", description = " 查询参数")
public class SysDropSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long uid;

  /** 空投类型：0按持有空投1拉新用户排名空投2指定用户空投 */
  @ApiModelProperty(
      value = "空投类型：0按持有空投1拉新用户排名空投2指定用户空投",
      example = "空投类型：0按持有空投1拉新用户排名空投2指定用户空投",
      required = false)
  private Integer droptype;

  /** 空投积分总值 */
  @ApiModelProperty(value = "空投积分总值", example = "空投积分总值", required = false)
  private Long dropintegralcount;

  /** 空投藏品id */
  @ApiModelProperty(value = "空投藏品id", example = "空投藏品id", required = false)
  private Long dropstreaid;
}
