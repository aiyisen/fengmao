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
 * DESC : 系统轮播 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统轮播 查询参数", description = "系统轮播 查询参数")
public class SysBannerSelectDto extends BaseEntity {

    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Long id;
        
        /**
     * 轮播图片路径
     */
    @ApiModelProperty(value = "轮播图片路径", example = "轮播图片路径", required = false)
    private String bannerPath;
        
        /**
     * 序号
     */
    @ApiModelProperty(value = "序号", example = "序号", required = false)
    private Integer orderSum;
        
        
        
        
        
        
}
