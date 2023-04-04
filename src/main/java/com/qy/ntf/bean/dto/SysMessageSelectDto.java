package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王振读
 * email 
 * Created on 2022-05-25 19:27:16
 * DESC : 系统信息 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统信息 查询参数", description = "系统信息 查询参数")
public class SysMessageSelectDto extends BaseEntity {

    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Long id;
        
        /**
     * 消息类型0系统通知1推送消息
     */
    @ApiModelProperty(value = "消息类型0系统通知1推送消息", example = "消息类型0系统通知1推送消息", required = false)
    private Integer msgType;
        
        /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", example = "消息内容", required = false)
    private String message;
        
        /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题", example = "消息标题", required = false)
    private String msgTitle;
        
        
        
        
        
        
}
