package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 藏品快照 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "藏品快照 修改参数", description = "藏品快照 修改参数")
public class OrderTreasureRecordUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 订单id */
  @ApiModelProperty(value = "订单id", example = "订单id", required = false)
  private Long orderTreasurePoolId;

  /** 宝贝类型0藏品首发1超前申购 */
  @ApiModelProperty(value = "宝贝类型0藏品首发1超前申购", example = "宝贝类型0藏品首发1超前申购", required = false)
  private Integer tType;

  /** 宝贝标题 */
  @ApiModelProperty(value = "宝贝标题", example = "宝贝标题", required = false)
  private String treasureTitle;

  /** 主图 */
  @ApiModelProperty(value = "主图", example = "主图", required = false)
  private String indexImgPath;

  /** 头部图片 */
  @ApiModelProperty(value = "头部图片", example = "头部图片", required = false)
  private String headImgPath;

  /** 总量 */
  @ApiModelProperty(value = "总量", example = "总量", required = false)
  private Integer totalCount;

  /** 剩余数量 */
  @ApiModelProperty(value = "剩余数量", example = "剩余数量", required = false)
  private Integer surplusCount;

  /** 冻结数量/已报名数量（超前申购属性名称） */
  @ApiModelProperty(
      value = "冻结数量/已报名数量（超前申购属性名称）",
      example = "冻结数量/已报名数量（超前申购属性名称）",
      required = false)
  private Integer frostCount;

  /** 价格(0RMB1META) */
  @ApiModelProperty(value = "价格(0RMB1META)", example = "价格(0RMB1META)", required = false)
  private BigDecimal price;

  /** 作品介绍 */
  @ApiModelProperty(value = "作品介绍", example = "作品介绍", required = false)
  private String introduce;

  /** 作品意义 */
  @ApiModelProperty(value = "作品意义", example = "作品意义", required = false)
  private String sense;

  /** 购买须知 */
  @ApiModelProperty(value = "购买须知", example = "购买须知", required = false)
  private String needKnow;

  /** 开售时间（藏品首发特有属性 */
  @ApiModelProperty(value = "开售时间（藏品首发特有属性", example = "开售时间（藏品首发特有属性", required = false)
  private Date saleTime;

  /** 报名时间（超前申购特有属性） */
  @ApiModelProperty(value = "报名时间（超前申购特有属性）", example = "报名时间（超前申购特有属性）", required = false)
  private Date upTime;

  /** 抽奖时间（超前申购特有属性） */
  @ApiModelProperty(value = "抽奖时间（超前申购特有属性）", example = "抽奖时间（超前申购特有属性）", required = false)
  private Date checkTime;
}
