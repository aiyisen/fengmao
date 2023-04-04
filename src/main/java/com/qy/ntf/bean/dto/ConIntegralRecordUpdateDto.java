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
 * DESC : 积分流水记录（记录总和>=0) 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "积分流水记录（记录总和>=0) 修改参数", description = "积分流水记录（记录总和>=0) 修改参数")
public class ConIntegralRecordUpdateDto extends BaseEntity {

    
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
     * 积分记录类型0登录赠送1邀请好友赠送2订单消费
     */
    @ApiModelProperty(value = "积分记录类型0登录赠送1邀请好友赠送2订单消费", example = "积分记录类型0登录赠送1邀请好友赠送2订单消费", required = false)
    private Integer recordType;
        
        /**
     * 涉及积分总量
     */
    @ApiModelProperty(value = "涉及积分总量", example = "涉及积分总量", required = false)
    private Integer metaCount;
        
        
        
        
        
        }
