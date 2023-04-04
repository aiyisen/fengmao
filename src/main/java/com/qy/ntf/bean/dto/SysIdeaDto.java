package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读
 * 2022-05-25 19:27:16
 * DESC : 反馈建议 添加参数
 */
@Data
@ApiModel(value = "反馈建议 添加参数", description = "反馈建议 添加参数")
public class SysIdeaDto extends BaseEntity {


    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = true)
    private Long id;
        
        /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", example = "用户id", required = true)
    private Long uId;
        
        /**
     * 意见反馈内容
     */
    @ApiModelProperty(value = "意见反馈内容", example = "意见反馈内容", required = true)
    private String ideaContent;
        
        
        
        
        
        
}
