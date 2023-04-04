package com.qy.ntf.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.param.TreaNeedParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 藏品首发/超前申购 主体 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "藏品首发/超前申购 主体 修改参数", description = "藏品首发/超前申购 主体 修改参数")
public class StoreTreasureUpdateDto extends BaseEntity {
  @ApiModelProperty(value = "超前申购藏品价格", example = "价格", required = true)
  @Min(value = 0, message = "价格最小为0")
  private BigDecimal zeroPrice;

  private Long sysSeriesId;

  @NotNull(message = " 不可为空")
  private Long sysCateId;

  private Long stripId;

  @ApiModelProperty(value = "合成藏品开始时间", example = "抽奖时间（超前申购特有属性）", required = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startTime;

  @ApiModelProperty(value = "合成藏品结束时间", example = "抽奖时间（超前申购特有属性）", required = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endTime;
  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;
  /** 藏品类型0藏品首发1超前申购 */
  @ApiModelProperty(value = "藏品类型0藏品首发1超前申购2合成藏品", example = "藏品类型0藏品首发1超前申购", required = false)
  private Integer tType;

  /** 藏品编号 */
  @ApiModelProperty(value = "藏品编号", example = "藏品编号", required = false)
  private String tNum;

  /** 藏品标题 */
  @ApiModelProperty(value = "藏品标题", example = "藏品标题", required = false)
  private String treasureTitle;

  /** 详情主图';'号按序拼接-作品故事 */
  @ApiModelProperty(value = "详情主图';'号按序拼接-作品故事", example = "详情主图';'号按序拼接-作品故事", required = false)
  private String indexImgPath;

  private Integer couldSale;

  private String detail;

  /** 头部图 */
  @ApiModelProperty(value = "头部图", example = "头部图", required = false)
  private String headImgPath;

  /** 头部类型0:jpg/jpeg/png 1:gif; 2:mp4/3gp/rmvb; 3:abc/fbx/dae/obj/bvh/dxf/psk/stl/ply/x3d */
  @ApiModelProperty(
      value = "头部类型0:jpg/jpeg/png 1:gif; 2:mp4/3gp/rmvb; 3:abc/fbx/dae/obj/bvh/dxf/psk/stl/ply/x3d",
      example =
          "头部类型0:jpg/jpeg/png 1:gif; 2:mp4/3gp/rmvb; 3:abc/fbx/dae/obj/bvh/dxf/psk/stl/ply/x3d",
      required = false)
  private Integer headType;

  /** 总量 */
  @ApiModelProperty(value = "总量", example = "总量", required = false)
  @Min(value = 0, message = "总量最小为0")
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
  @ApiModelProperty(
      value = "价格(0RMB1META)超前申购为参与抽签积分值",
      example = "价格(0RMB1META)",
      required = false)
  @Min(value = 0, message = "价格、积分价格 最小为0")
  private BigDecimal price;

  /** 作品介绍 */
  @ApiModelProperty(value = "作品介绍", example = "作品介绍", required = false)
  private String introduce;

  private String authInfo;
  private String linkInfo;

  /** 作品意义 */
  @ApiModelProperty(value = "作品意义", example = "作品意义", required = false)
  private String sense;

  /** 购买须知 */
  @ApiModelProperty(value = "购买须知", example = "购买须知", required = false)
  private String needKnow;

  /** 开售时间（藏品首发特有属性 */
  @ApiModelProperty(value = "开售时间（藏品首发特有属性", example = "开售时间（藏品首发特有属性", required = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date saleTime;

  /** 报名开始时间（超前申购特有属性） */
  @ApiModelProperty(
      value = "报名开始时间（超前申购特有属性）合成藏品开始时间",
      example = "报名开始时间（超前申购特有属性）",
      required = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull
  private Date upTime;

  /** 报名截止时间（超亲申购特有属性） */
  @ApiModelProperty(
      value = "报名截止时间（超亲申购特有属性）合成藏品结束时间",
      example = "报名截止时间（超亲申购特有属性）",
      required = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date downTime;

  /** 抽奖时间（超前申购特有属性） */
  @ApiModelProperty(value = "抽奖时间（超前申购特有属性）", example = "抽奖时间（超前申购特有属性）", required = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date checkTime;

  private Integer couldCompound;

  private List<TreaNeedParam> neededTreId;
  private Long sysOrgId;
  private SysOrgDto sysOrgDto;
  /** 限购数量 */
  private Integer ruleCount;

  private BigDecimal turnMaxPrice;
  private BigDecimal turnMinPrice;
}
