package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 平台信息 添加参数
 */
@Data
@ApiModel(value = "平台信息 添加参数", description = "平台信息 添加参数")
public class SysPlateConfigAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 用户协议（富文本） */
  @ApiModelProperty(value = "用户协议（富文本）", example = "用户协议（富文本）", required = true)
  private String userAgreement;

  /** 隐私协议（富文本） */
  @ApiModelProperty(value = "隐私协议（富文本）", example = "隐私协议（富文本）", required = true)
  private String privacyPolicy;

  /** 运营商协议（富文本） */
  @ApiModelProperty(value = "运营商协议（富文本）", example = "运营商协议（富文本）", required = true)
  private String mobilePolicy;

  /** 新手指南（富文本） */
  @ApiModelProperty(value = "新手指南（富文本）", example = "新手指南（富文本）", required = true)
  private String newbieGuide;

  /** 关于我们（富文本） */
  @ApiModelProperty(value = "关于我们（富文本）", example = "关于我们（富文本）", required = true)
  private String aboutUs;

  private String inviteInfo;
}
