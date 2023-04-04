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
 * DESC : 特权商品材料模板 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "特权商品材料模板 修改参数", description = "特权商品材料模板 修改参数")
public class StoreProductTemplateUpdateDto extends BaseEntity {

    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Long id;
        
        /**
     * 特权商品id
     */
    @ApiModelProperty(value = "特权商品id", example = "特权商品id", required = false)
    private Long productId;
        
        /**
     * 所需商品id
     */
    @ApiModelProperty(value = "所需商品id", example = "所需商品id", required = false)
    private Long needId;
        
        /**
     * 所需商品类型0虚拟商品1实物商品
     */
    @ApiModelProperty(value = "所需商品类型0虚拟商品1实物商品", example = "所需商品类型0虚拟商品1实物商品", required = false)
    private Integer needType;
        
        
        
        
        
        }
