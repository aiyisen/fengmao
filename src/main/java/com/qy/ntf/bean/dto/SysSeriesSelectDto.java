package com.qy.ntf.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author 王振读 email Created on 2022-07-15 19:26:43 DESC : 系列主体 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系列主体 查询参数", description = "系列主体 查询参数")
public class SysSeriesSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 发行方id */
  @ApiModelProperty(value = "发行方id", example = "发行方id", required = false)
  private Long sysorgid;

  /** 系列名称 */
  @ApiModelProperty(value = "系列名称", example = "系列名称", required = false)
  private String seriesname;

  /** 系列详情 */
  @ApiModelProperty(value = "系列详情", example = "系列详情", required = false)
  private String seriesdetail;

  /** 开始时间 */
  @ApiModelProperty(value = "开始时间", example = "开始时间", required = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date starttime;

  /** 截止时间 */
  @ApiModelProperty(value = "截止时间", example = "截止时间", required = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date endtime;

  /** 背景颜色值 */
  @ApiModelProperty(value = "背景颜色值", example = "背景颜色值", required = false)
  private String bgcolor;

  /** 首图地址 */
  @ApiModelProperty(value = "首图地址", example = "首图地址", required = false)
  private String indeximg;
}
