package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品首发/超前申购 主体 添加参数
 */
@Data
@ApiModel(value = "藏品首发/超前申购 主体 添加参数", description = "藏品首发/超前申购 主体 添加参数")
public class BindBoxItemAddDto extends BaseEntity implements Serializable {

  /** */
  private Long id;

  private Long pId;

  /** 藏品编号 */
  @ApiModelProperty(value = "藏品编号", example = "藏品编号", required = true)
  private String tNum;

  /** 藏品标题 */
  @ApiModelProperty(value = "藏品标题", example = "藏品标题", required = true)
  private String treasureTitle;

  /** 列表图 1张 */
  private String indexImgPath;

  private Integer couldSale;
  // 作品故事，详情图多张
  private String detail;

  /** 头部图 */
  @ApiModelProperty(value = "详情主图", example = "详情主图", required = true)
  private String headImgPath;

  /** 总量 */
  @ApiModelProperty(value = "总量", example = "总量", required = true)
  @Min(value = 0, message = "总量最少为0")
  private Integer totalCount;

  private Long sysOrgId;
  /** 作品意义 */
  private String sense;

  private String introduce;

  private String authInfo;
  private String linkInfo;

  @NotNull(message = " 不可为空")
  private Long sysCateId;
}
