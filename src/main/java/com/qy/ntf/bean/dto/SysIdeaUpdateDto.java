package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王振读
 * email 
 * Created on 2022-05-25 19:27:16
 * DESC : 反馈建议 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "反馈建议 修改参数", description = "反馈建议 修改参数")
public class SysIdeaUpdateDto extends BaseEntity {

    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Long id;
        
        /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", example = "用户id", required = false)
    private Long uId;
        
        /**
     * 意见反馈内容
     */
    @ApiModelProperty(value = "意见反馈内容", example = "意见反馈内容", required = false)
    private String ideaContent;
        
        
        
        
        
        }
