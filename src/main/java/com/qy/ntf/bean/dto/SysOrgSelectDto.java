package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 王振读 email Created on 2022-07-05 20:10:31 DESC : 发行方 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "发行方 查询参数", description = "发行方 查询参数")
public class SysOrgSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 发行方名称 */
  @ApiModelProperty(value = "发行方名称", example = "发行方名称", required = false)
  private String orgname;

  /** 发行方头像地址 */
  @ApiModelProperty(value = "发行方头像地址", example = "发行方头像地址", required = false)
  private String orgimg;

  /** 创建者ID */
  @ApiModelProperty(value = "创建者ID", example = "创建者ID", required = false)
  private Long createid;

  /** 创建时间 */
  @ApiModelProperty(value = "创建时间", example = "创建时间", required = false)
  private Date createtime;

  /** 修改者id */
  @ApiModelProperty(value = "修改者id", example = "修改者id", required = false)
  private Long updateid;

  /** 修改时间 */
  @ApiModelProperty(value = "修改时间", example = "修改时间", required = false)
  private Date updatetime;
}
