package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统轮播 添加参数
 */
@Data
@ApiModel(value = "系统轮播 添加参数", description = "系统轮播 添加参数")
public class SysBannerDto extends BaseEntity {

  private Integer bannerType;

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 轮播图片路径 */
  @ApiModelProperty(value = "轮播图片路径", example = "轮播图片路径", required = true)
  private String bannerPath;

  /** 序号 */
  @ApiModelProperty(value = "序号", example = "序号", required = true)
  private Integer orderSum;

  private String linkUrl;
}
