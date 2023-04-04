package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读
 * 2022-05-25 19:27:16
 * DESC : 帮助中心 添加参数
 */
@Data
@ApiModel(value = "帮助中心 添加参数", description = "帮助中心 添加参数")
public class SysHelpAddDto extends BaseEntity {


    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = true)
    private Long id;
        
        /**
     * 标题
     */
    @ApiModelProperty(value = "标题", example = "标题", required = true)
    private String queTitle;
        
        /**
     * 内容（富文本）
     */
    @ApiModelProperty(value = "内容（富文本）", example = "内容（富文本）", required = true)
    private String queAns;
        
        
        
        
        
        
}
