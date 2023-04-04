package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品快照 添加参数
 */
@Data
@ApiModel(value = "实物商品快照 添加参数", description = "实物商品快照 添加参数")
public class OrderProductRecordAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** order_product.id */
  @ApiModelProperty(value = "order_product.id", example = "order_product.id", required = true)
  private Long orderProductId;

  /** 商品类型0普通商品1特权专区2兑换专区 */
  @ApiModelProperty(value = "商品类型0普通商品1特权专区2兑换专区", example = "商品类型0普通商品1特权专区2兑换专区", required = true)
  private Integer proType;

  /** 商品标题 */
  @ApiModelProperty(value = "商品标题", example = "商品标题", required = true)
  private String proTitle;

  /** 原价 */
  @ApiModelProperty(value = "原价", example = "原价", required = true)
  private BigDecimal oldPrice;

  /** 现价 */
  @ApiModelProperty(value = "现价", example = "现价", required = true)
  private BigDecimal curPrice;

  /** 详情（富文本） */
  @ApiModelProperty(value = "详情（富文本）", example = "详情（富文本）", required = true)
  private String proDetail;

  /** 头部图片 */
  @ApiModelProperty(value = "头部图片", example = "头部图片", required = true)
  private String headerIndex;

  /** 总量 */
  @ApiModelProperty(value = "总量", example = "总量", required = true)
  private Integer totalCount;

  /** 剩余数量 */
  @ApiModelProperty(value = "剩余数量", example = "剩余数量", required = true)
  private Integer surplusCount;

  /** 冻结数量/已报名数量（超前申购属性名称） */
  @ApiModelProperty(
      value = "冻结数量/已报名数量（超前申购属性名称）",
      example = "冻结数量/已报名数量（超前申购属性名称）",
      required = true)
  private Integer frostCount;
}
