package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品首发/超前申购 主体 实体
 */
@TableName("store_treasure")
@Data
public class StoreTreasure extends BaseEntity {

  /** */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  private Long pId;
  private Long sysCateId;
  private Long sysSeriesId;

  @TableField("s_t_r_i_p_id")
  private Long stripId;

  /** 藏品类型0藏品首发1超前申购 2合成藏品3盲盒主藏品4盲盒子藏品 */
  private Integer tType;

  private Integer rType;
  private String linkAddress;

  /** 藏品编号 */
  private String tNum;

  /** 藏品标题 */
  private String treasureTitle;

  /** 详情主图";"号按序拼接-作品故事 */
  @TableField("indexImgPath")
  private String indexImgPath;

  private String detail;

  /** 头部图 */
  @TableField("headImgPath")
  private String headImgPath;

  /** 头部类型0:jpg/jpeg/png 1:gif; 2:mp4/3gp/rmvb; 3:abc/fbx/dae/obj/bvh/dxf/psk/stl/ply/x3d */
  private Integer headType;

  /** 总量 */
  private Integer totalCount;

  /** 剩余数量 */
  private Integer surplusCount;

  /** 冻结数量/已报名数量（超前申购属性名称） */
  private Integer frostCount;

  /** 价格(0RMB1META) */
  private BigDecimal price;

  private BigDecimal zeroPrice;

  /** 作品介绍 */
  private String introduce;

  private String authInfo;
  private String linkInfo;

  /** 作品意义 */
  private String sense;

  /** 购买须知 */
  private String needKnow;

  private String transationId;
  private String ddcUrl;
  private String ddcId;
  private String orderFingerprint;

  /** 开售时间（藏品首发特有属性 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date saleTime;

  /** 报名开始时间（超前申购特有属性） */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date upTime;

  /** 报名截止时间（超亲申购特有属性） */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date downTime;

  /** 抽奖时间（超前申购特有属性） */
  private Date checkTime;
  /** 是否允许合成0否1是 */
  private Integer couldCompound;

  private Integer couldSale;

  private String tagCon;

  private String beforeRule;
  private Long sysOrgId;
  /** 限购数量 */
  private Integer ruleCount;

  private Integer fromUser;
  // 是否已经出售1是0否 2已赠送他人
  private Integer isSale;
  private String allNftId;
  private BigDecimal turnMaxPrice;
  private BigDecimal turnMinPrice;
}
