package com.qy.ntf.bean.customResult;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.bean.entity.SysOrg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class MyTreasure {
  // 藏品id 聚合池id
  private Long id;

  @ApiModelProperty("标题")
  private String title;

  @ApiModelProperty("是否在出售：0否1是")
  private Integer isSaling;
  // 藏品或聚合池id
  private Long sTreasureId;

  private Integer totalCount;
  private Integer salingCount;
  //  来源0藏品1聚合池
  private Integer transFrom;

  @ApiModelProperty("首图")
  private String headImg;

  @ApiModelProperty("指纹")
  private String orderFingerprint;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createTime;

  private StoreTreasure storeTreasure;
  private String tagCon;
  private String ddcId;
  private String transationId;
  private String ddcUrl;
  private BigDecimal proPrice;
  private SysOrg sysOrg;
  private List<StoreTreasure> children;
  // null 查询藏品，1盲盒
  private Integer isCheck;
  private Boolean isCompund;
}
