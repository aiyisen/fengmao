package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 收货地址 添加参数
 */
@Data
@ApiModel(value = "收货地址 添加参数", description = "收货地址 添加参数")
public class SysAddressAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = true)
  private Long uId;

  /** 省级/直辖市id */
  private String username;

  private String provName;
  private String cityName;
  private String conName;

  /** 详细 */
  @ApiModelProperty(value = "详细", example = "详细", required = true)
  private String detail;

  private String mobile;
}
