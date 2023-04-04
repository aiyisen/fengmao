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
 * DESC : 帮助中心 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "帮助中心 修改参数", description = "帮助中心 修改参数")
public class SysHelpUpdateDto extends BaseEntity {

    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Long id;
        
        /**
     * 标题
     */
    @ApiModelProperty(value = "标题", example = "标题", required = false)
    private String queTitle;
        
        /**
     * 内容（富文本）
     */
    @ApiModelProperty(value = "内容（富文本）", example = "内容（富文本）", required = false)
    private String queAns;
        
        
        
        
        
        }
