package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 收货地址 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "收货地址 修改参数", description = "收货地址 修改参数")
public class SysAddressUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = false)
  private Long uId;

  private String provName;
  private String cityName;
  private String conName;

  /** 详细 */
  @ApiModelProperty(value = "详细", example = "详细", required = false)
  private String detail;

  private String mobile;
  private String username;
}
