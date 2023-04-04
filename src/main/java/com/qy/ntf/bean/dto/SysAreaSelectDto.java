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
 * DESC : 全国区域信息表 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "全国区域信息表 查询参数", description = "全国区域信息表 查询参数")
public class SysAreaSelectDto extends BaseEntity {

    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Integer id;
        
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private String areaName;
        
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Integer pid;
        
}
