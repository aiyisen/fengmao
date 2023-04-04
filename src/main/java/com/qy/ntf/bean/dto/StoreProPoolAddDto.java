package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池主体 添加参数
 */
@Data
@ApiModel(value = "聚合池主体 添加参数", description = "聚合池主体 添加参数")
public class StoreProPoolAddDto extends BaseEntity {
  private String headImgPath;
  private Integer headType;
  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 聚合池商品标题 */
  @ApiModelProperty(value = "聚合池商品标题", example = "聚合池商品标题", required = true)
  private String proPoolTitle;

  /** 首图地址 */
  @ApiModelProperty(value = "首图地址", example = "首图地址", required = true)
  private String indexPath;

  /** 企业名称 */
  @ApiModelProperty(value = "企业名称", example = "企业名称", required = true)
  private String orgName;

  /** 企业头像 */
  @ApiModelProperty(value = "企业头像", example = "企业头像", required = true)
  private String orgIndexPath;

  /** 价格 */
  @ApiModelProperty(value = "价格", example = "价格", required = true)
  private BigDecimal proPrice;

  /** 标签";"分割 */
  @ApiModelProperty(value = "标签';'分割", example = "标签';'分割", required = true)
  private String tagCon;

  private String orderFingerprint;

  /** 藏品编号 */
  @ApiModelProperty(value = "藏品编号", example = "藏品编号", required = true)
  private String proNum;

  private Integer fromType;
  private Long sysOrgId;
}
