package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统信息 添加参数
 */
@Data
@ApiModel(value = "系统信息 添加参数", description = "系统信息 添加参数")
public class SysMessageDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 消息类型0系统通知1推送消息 */
  @ApiModelProperty(value = "消息类型0系统通知1推送消息", example = "消息类型0系统通知1推送消息", required = true)
  private Integer msgType;

  /** 消息内容 */
  @ApiModelProperty(value = "消息内容", example = "消息内容", required = true)
  private String message;

  private String logNum;

  /** 消息标题 */
  @ApiModelProperty(value = "消息标题", example = "消息标题", required = true)
  private String msgTitle;
}
