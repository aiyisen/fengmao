package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 聚合池主体 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "聚合池主体 查询参数", description = "聚合池主体 查询参数")
public class StoreProPoolSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 聚合池商品标题 */
  @ApiModelProperty(value = "聚合池商品标题", example = "聚合池商品标题", required = false)
  private String proPoolTitle;

  /** 首图地址 */
  @ApiModelProperty(value = "首图地址", example = "首图地址", required = false)
  private String indexPath;

  /** 企业名称 */
  @ApiModelProperty(value = "企业名称", example = "企业名称", required = false)
  private String orgName;

  /** 企业头像 */
  @ApiModelProperty(value = "企业头像", example = "企业头像", required = false)
  private String orgIndexPath;

  /** 价格 */
  @ApiModelProperty(value = "价格", example = "价格", required = false)
  private BigDecimal proPrice;

  /** 标签";"分割 */
  @ApiModelProperty(value = "标签';'分割", example = "标签';'分割", required = false)
  private String tagCon;

  /** 藏品编号 */
  @ApiModelProperty(value = "藏品编号", example = "藏品编号", required = false)
  private String proNum;
}
