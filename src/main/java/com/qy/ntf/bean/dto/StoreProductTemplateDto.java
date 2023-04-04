package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读
 * 2022-05-25 19:27:16
 * DESC : 特权商品材料模板 添加参数
 */
@Data
@ApiModel(value = "特权商品材料模板 添加参数", description = "特权商品材料模板 添加参数")
public class StoreProductTemplateDto extends BaseEntity {


    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = true)
    private Long id;
        
        /**
     * 特权商品id
     */
    @ApiModelProperty(value = "特权商品id", example = "特权商品id", required = true)
    private Long productId;
        
        /**
     * 所需商品id
     */
    @ApiModelProperty(value = "所需商品id", example = "所需商品id", required = true)
    private Long needId;
        
        /**
     * 所需商品类型0虚拟商品1实物商品
     */
    @ApiModelProperty(value = "所需商品类型0虚拟商品1实物商品", example = "所需商品类型0虚拟商品1实物商品", required = true)
    private Integer needType;
        
        
        
        
        
        
}
