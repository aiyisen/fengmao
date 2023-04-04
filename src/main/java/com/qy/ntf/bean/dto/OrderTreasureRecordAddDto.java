package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品快照 添加参数
 */
@Data
@ApiModel(value = "藏品快照 添加参数", description = "藏品快照 添加参数")
public class OrderTreasureRecordAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 订单id */
  @ApiModelProperty(value = "订单id", example = "订单id", required = true)
  private Long orderTreasurePoolId;

  /** 宝贝类型0藏品首发1超前申购 */
  @ApiModelProperty(value = "宝贝类型0藏品首发1超前申购", example = "宝贝类型0藏品首发1超前申购", required = true)
  private Integer tType;

  /** 宝贝标题 */
  @ApiModelProperty(value = "宝贝标题", example = "宝贝标题", required = true)
  private String treasureTitle;

  /** 主图 */
  @ApiModelProperty(value = "主图", example = "主图", required = true)
  private String indexImgPath;

  /** 头部图片 */
  @ApiModelProperty(value = "头部图片", example = "头部图片", required = true)
  private String headImgPath;

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

  /** 价格(0RMB1META) */
  @ApiModelProperty(value = "价格(0RMB1META)", example = "价格(0RMB1META)", required = true)
  private BigDecimal price;

  /** 作品介绍 */
  @ApiModelProperty(value = "作品介绍", example = "作品介绍", required = true)
  private String introduce;

  /** 作品意义 */
  @ApiModelProperty(value = "作品意义", example = "作品意义", required = true)
  private String sense;

  /** 购买须知 */
  @ApiModelProperty(value = "购买须知", example = "购买须知", required = true)
  private String needKnow;

  /** 开售时间（藏品首发特有属性 */
  @ApiModelProperty(value = "开售时间（藏品首发特有属性", example = "开售时间（藏品首发特有属性", required = true)
  private Date saleTime;

  /** 报名时间（超前申购特有属性） */
  @ApiModelProperty(value = "报名时间（超前申购特有属性）", example = "报名时间（超前申购特有属性）", required = true)
  private Date upTime;

  /** 抽奖时间（超前申购特有属性） */
  @ApiModelProperty(value = "抽奖时间（超前申购特有属性）", example = "抽奖时间（超前申购特有属性）", required = true)
  private Date checkTime;
}
