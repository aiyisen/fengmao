package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 权益商城-普通专区/特权专区/兑换专区 主体 添加参数
 */
@Data
@ApiModel(value = "权益商城-普通专区/特权专区/兑换专区 主体 添加参数", description = "权益商城-普通专区/特权专区/兑换专区 主体 添加参数")
public class StoreProductAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 商品类型0普通商品1特权专区2兑换专区 */
  @ApiModelProperty(value = "商品类型0普通商品1特权专区2兑换专区", example = "商品类型0普通商品1特权专区2兑换专区", required = true)
  @NotNull
  private Integer proType;

  /** 商品标题 */
  @ApiModelProperty(value = "商品标题", example = "商品标题", required = true)
  @NotEmpty
  private String proTitle;

  /** 原价 */
  @ApiModelProperty(value = "原价", example = "原价", required = true)
  @NotNull
  @Min(value = 0, message = "原价最小为0")
  private BigDecimal oldPrice;

  /** 现价 */
  @ApiModelProperty(value = "现价", example = "现价", required = true)
  @NotNull
  @Min(value = 0, message = "现价最小为0")
  private BigDecimal curPrice;

  /** 详情“;”按序拼接 */
  @ApiModelProperty(value = "详情“;”按序拼接", example = "详情“;”按序拼接", required = true)
  @NotEmpty
  private String proDetail;

  /** 头部图片 */
  @ApiModelProperty(value = "头部图片", example = "头部图片", required = true)
  @NotEmpty
  private String headerIndex;

  @ApiModelProperty("轮播图;拼接")
  @NotEmpty(message = "banners 不能为空")
  private String banners;

  /** 总量 */
  @ApiModelProperty(value = "总量", example = "总量", required = true)
  @NotNull(message = "totalCount 不能为空")
  @Min(value = 0, message = "总量最小为0")
  private Integer totalCount;

  /** 剩余数量 */
  @ApiModelProperty(value = "剩余数量", example = "剩余数量")
  private Integer surplusCount;

  /** 运费 */
  @ApiModelProperty(value = "运费", example = "运费")
  @Min(value = 0, message = "运费最小为0")
  private BigDecimal feight;

  /** 冻结数量/已报名数量（超前申购属性名称） */
  @ApiModelProperty(value = "冻结数量/已报名数量（超前申购属性名称）", example = "冻结数量/已报名数量（超前申购属性名称）")
  private Integer frostCount;
  /** vip折扣 */
  @Min(value = 0, message = "运费最小为0")
  private BigDecimal vipPercent;
}
