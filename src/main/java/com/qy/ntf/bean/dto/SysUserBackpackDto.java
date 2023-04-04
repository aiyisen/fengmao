package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 用户背包（记录虚拟商品购买订单标识） 添加参数
 */
@Data
@ApiModel(value = "用户背包（记录虚拟商品购买订单标识） 添加参数", description = "用户背包（记录虚拟商品购买订单标识） 添加参数")
public class SysUserBackpackDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 用户id */
  @ApiModelProperty(value = "用户id", example = "用户id", required = true)
  private Long uId;

  /** 藏品id */
  @ApiModelProperty(value = "藏品id", example = "藏品id", required = true)
  private Long sTreasureId;

  /** 订单指纹 */
  @ApiModelProperty(value = "订单指纹", example = "订单指纹", required = true)
  private String orderFingerprint;

  /** 记录类型：1赠送2获赠3空投4支出 */
  @ApiModelProperty(value = "记录类型：1赠送2获赠3空投4支出", example = "记录类型：1赠送2获赠3空投4支出", required = true)
  private Integer finType;

  private String ddcId;
  private String ddcUrl;
}
