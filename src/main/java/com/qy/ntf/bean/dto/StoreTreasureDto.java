package com.qy.ntf.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.customResult.NeededRes;
import com.qy.ntf.bean.entity.StoreTreIosPrice;
import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.bean.entity.SysOrg;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.TreaNeedParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品首发/超前申购 主体 添加参数
 */
@Data
@ApiModel(value = "藏品首发/超前申购 主体 添加参数", description = "藏品首发/超前申购 主体 添加参数")
public class StoreTreasureDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  private Long pId;
  private Long sysCateId;
  private Long sysSeriesId;
  private Long stripId;
  private StoreTreIosPrice storeTreIosPrice;

  /** 藏品类型0藏品首发1超前申购 */
  @ApiModelProperty(
      value = "藏品类型0藏品首发1超前申购 2合成藏品3盲盒主藏品4盲盒子藏品",
      example = "藏品类型0藏品首发1超前申购",
      required = true)
  private Integer tType;

  private Integer rType;
  private String linkAddress;

  /** 藏品编号 */
  @ApiModelProperty(value = "藏品编号", example = "藏品编号", required = true)
  private String tNum;

  /** 藏品标题 */
  @ApiModelProperty(value = "藏品标题", example = "藏品标题", required = true)
  private String treasureTitle;

  /** 详情主图";"号按序拼接-作品故事 */
  @ApiModelProperty(value = "详情主图';'号按序拼接-作品故事", example = "详情主图';'号按序拼接-作品故事", required = true)
  private String indexImgPath;

  private String detail;

  /** 头部图 */
  @ApiModelProperty(value = "头部图", example = "头部图", required = true)
  private String headImgPath;

  /** 头部类型0:jpg/jpeg/png 1:gif; 2:mp4/3gp/rmvb; 3:abc/fbx/dae/obj/bvh/dxf/psk/stl/ply/x3d */
  @ApiModelProperty(
      value = "头部类型0:jpg/jpeg/png 1:gif; 2:mp4/3gp/rmvb; 3:abc/fbx/dae/obj/bvh/dxf/psk/stl/ply/x3d",
      example =
          "头部类型0:jpg/jpeg/png 1:gif; 2:mp4/3gp/rmvb; 3:abc/fbx/dae/obj/bvh/dxf/psk/stl/ply/x3d",
      required = true)
  private Integer headType;

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
  @ApiModelProperty(value = "价格(0RMB1META)超前申购为参与抽签积分值", example = "价格(0RMB1META)", required = true)
  private BigDecimal price;

  @ApiModelProperty(value = "超前申购实际价格", example = "价格(0RMB1META)", required = true)
  private BigDecimal zeroPrice;

  /** 作品介绍 */
  @ApiModelProperty(value = "作品介绍", example = "作品介绍", required = true)
  private String introduce;

  private String authInfo;
  private String linkInfo;
  private Integer couldSale;
  /** 作品意义 */
  @ApiModelProperty(value = "作品意义", example = "作品意义", required = true)
  private String sense;

  /** 购买须知 */
  @ApiModelProperty(value = "购买须知", example = "购买须知", required = true)
  private String needKnow;

  private String transationId;
  private String ddcUrl;
  private String ddcId;

  /** 开售时间（藏品首发特有属性 */
  @ApiModelProperty(value = "开售时间（藏品首发特有属性", example = "开售时间（藏品首发特有属性", required = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date saleTime;

  /** 报名开始时间（超前申购特有属性） */
  @ApiModelProperty(
      value = "报名开始时间（超前申购特有属性）合成藏品截止时间",
      example = "报名开始时间（超前申购特有属性）合成藏品截止时间",
      required = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date upTime;

  /** 报名截止时间（超亲申购特有属性） */
  @ApiModelProperty(
      value = "报名截止时间（超亲申购特有属性）合成藏品截止时间",
      example = "报名截止时间（超亲申购特有属性）合成藏品截止时间",
      required = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date downTime;

  /** 抽奖时间（超前申购特有属性） */
  @ApiModelProperty(value = "抽奖时间（超前申购特有属性）", example = "抽奖时间（超前申购特有属性）", required = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date checkTime;

  @ApiModelProperty("是否已购买")
  private Boolean isBuy;

  @ApiModelProperty("0未开售1已开售")
  private Integer isDone;

  private Integer couldCompound;

  private SysUser creator;
  private String beforeRule;
  private Integer needCount;
  private Integer checkState;
  private Long sysOrgId;
  private SysOrg sysOrg;
  /** 限购数量 */
  private Integer ruleCount;

  private List<NeededRes> neededRes;
  private List<TreaNeedParam> neededTreId;
  // 盲盒特有属性
  private List<StoreTreasure> children;

  private String givingUser;
  private String reciveUser;
  private Integer hasBefore;
  private Integer hashCount;
  private BigDecimal turnMaxPrice;
  private BigDecimal turnMinPrice;
}
