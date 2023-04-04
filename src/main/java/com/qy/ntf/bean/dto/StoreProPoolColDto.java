package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读
 * 2022-05-25 19:27:16
 * DESC : 聚合池藏品收藏 添加参数
 */
@Data
@ApiModel(value = "聚合池藏品收藏 添加参数", description = "聚合池藏品收藏 添加参数")
public class StoreProPoolColDto extends BaseEntity {


    
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
     * 聚合池商品id
     */
    @ApiModelProperty(value = "聚合池商品id", example = "聚合池商品id", required = true)
    private Long proPoolId;
        
        
        
        
        
        
}
