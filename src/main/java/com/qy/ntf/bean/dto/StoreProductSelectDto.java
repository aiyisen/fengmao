package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 权益商城-普通专区/特权专区/兑换专区 主体 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "权益商城-普通专区/特权专区/兑换专区 主体 查询参数", description = "权益商城-普通专区/特权专区/兑换专区 主体 查询参数")
public class StoreProductSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 商品类型0普通商品1特权专区2兑换专区 */
  @ApiModelProperty(
      value = "商品类型0普通商品1特权专区2兑换专区",
      example = "商品类型0普通商品1特权专区2兑换专区",
      required = false)
  private Integer proType;

  /** 商品标题 */
  @ApiModelProperty(value = "商品标题", example = "商品标题", required = false)
  private String proTitle;

  /** 原价 */
  @ApiModelProperty(value = "原价", example = "原价", required = false)
  private BigDecimal oldPrice;

  /** 现价 */
  @ApiModelProperty(value = "现价", example = "现价", required = false)
  private BigDecimal curPrice;

  /** 详情“;”按序拼接 */
  @ApiModelProperty(value = "详情“;”按序拼接", example = "详情“;”按序拼接", required = false)
  private String proDetail;

  /** 头部图片 */
  @ApiModelProperty(value = "头部图片", example = "头部图片", required = false)
  private String headerIndex;

  @ApiModelProperty("轮播图;拼接")
  private String banners;
  /** 总量 */
  @ApiModelProperty(value = "总量", example = "总量", required = false)
  private Integer totalCount;

  /** 剩余数量 */
  @ApiModelProperty(value = "剩余数量", example = "剩余数量", required = false)
  private Integer surplusCount;

  /** 运费 */
  @ApiModelProperty(value = "运费", example = "运费", required = false)
  private BigDecimal feight;

  /** 冻结数量/已报名数量（超前申购属性名称） */
  @ApiModelProperty(
      value = "冻结数量/已报名数量（超前申购属性名称）",
      example = "冻结数量/已报名数量（超前申购属性名称）",
      required = false)
  private Integer frostCount;
}
