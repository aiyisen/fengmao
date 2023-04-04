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
 * DESC : 超亲申购中奖名单 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "超亲申购中奖名单 查询参数", description = "超亲申购中奖名单 查询参数")
public class StoreTreasureCheckUserSelectDto extends BaseEntity {

    
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
     * 藏品id
     */
    @ApiModelProperty(value = "藏品id", example = "藏品id", required = false)
    private Long storeTreasureId;
        
        
        
        
        
        
}
