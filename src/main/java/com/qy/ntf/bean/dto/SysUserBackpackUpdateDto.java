package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 用户背包（记录虚拟商品购买订单标识） 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户背包（记录虚拟商品购买订单标识） 修改参数", description = "用户背包（记录虚拟商品购买订单标识） 修改参数")
public class SysUserBackpackUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = false)
  private Long uId;

  /** 藏品id */
  @ApiModelProperty(value = "藏品id", example = "藏品id", required = false)
  private Long sTreasureId;

  /** 订单指纹 */
  @ApiModelProperty(value = "订单指纹", example = "订单指纹", required = false)
  private String orderFingerprint;

  /** 记录类型：1赠送2获赠3空投4支出 */
  @ApiModelProperty(value = "记录类型：1赠送2获赠3空投4支出", example = "记录类型：1赠送2获赠3空投4支出", required = false)
  private Integer finType;

  private String ddcId;
  private String ddcUrl;
}
