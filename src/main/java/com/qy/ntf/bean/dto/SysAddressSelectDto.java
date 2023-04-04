package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 收货地址 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "收货地址 查询参数", description = "收货地址 查询参数")
public class SysAddressSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  private String username;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = false)
  private Long uId;

  /** 省级/直辖市id */
  @ApiModelProperty(value = "省级/直辖市id", example = "省级/直辖市id", required = false)
  private Integer provId;

  /** 市级id */
  @ApiModelProperty(value = "市级id", example = "市级id", required = false)
  private Integer cityId;

  /** 县级id */
  @ApiModelProperty(value = "县级id", example = "县级id", required = false)
  private Integer conId;

  /** 详细 */
  @ApiModelProperty(value = "详细", example = "详细", required = false)
  private String detail;
}
