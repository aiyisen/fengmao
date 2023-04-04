package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读
 * 2022-05-25 19:27:16
 * DESC : 超亲申购中奖名单 添加参数
 */
@Data
@ApiModel(value = "超亲申购中奖名单 添加参数", description = "超亲申购中奖名单 添加参数")
public class StoreTreasureCheckUserDto extends BaseEntity {


    
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
     * 藏品id
     */
    @ApiModelProperty(value = "藏品id", example = "藏品id", required = true)
    private Long storeTreasureId;
        
        
        
        
        
        
}
