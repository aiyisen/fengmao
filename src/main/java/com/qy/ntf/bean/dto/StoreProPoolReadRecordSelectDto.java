package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王振读 email Created on 2022-05-28 00:02:44 DESC : 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = " 查询参数", description = " 查询参数")
public class StoreProPoolReadRecordSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 聚合池id */
  @ApiModelProperty(value = "聚合池id", example = "聚合池id", required = false)
  private Long storeProPoolId;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = false)
  private Long uId;
}
