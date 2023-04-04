package com.qy.ntf.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 王振读 email Created on 2022-07-15 19:26:43 DESC : 系列主体 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系列主体 修改参数", description = "系列主体 修改参数")
public class SysSeriesUpdateDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 发行方id */
  @ApiModelProperty(value = "发行方id", example = "发行方id", required = false)
  @NotNull(message = "发行方id不可为空")
  private Long sysorgid;

  /** 系列名称 */
  @ApiModelProperty(value = "系列名称", example = "系列名称", required = false)
  @NotEmpty(message = "系列名称不可为空")
  private String seriesname;

  /** 系列详情 */
  @ApiModelProperty(value = "系列详情", example = "系列详情", required = false)
  @NotEmpty(message = "系列详情不可为空")
  private String seriesdetail;

  /** 开始时间 */
  @ApiModelProperty(value = "开始时间", example = "开始时间", required = false)
  @NotNull(message = "开始时间不可为空")
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
  @NotEmpty(message = "背景颜色值不可为空")
  private String bgcolor;

  /** 首图地址 */
  @ApiModelProperty(value = "首图地址", example = "首图地址", required = false)
  @NotEmpty(message = "首图地址不可为空")
  private String indeximg;
}
