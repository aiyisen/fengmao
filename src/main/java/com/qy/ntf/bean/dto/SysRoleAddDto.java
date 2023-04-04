package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读
 * 2022-05-25 19:27:16
 * DESC : 系统角色 添加参数
 */
@Data
@ApiModel(value = "系统角色 添加参数", description = "系统角色 添加参数")
public class SysRoleAddDto extends BaseEntity {


    
        /**
     * 主键
     */
    @ApiModelProperty(value = "主键", example = "主键", required = true)
    private Integer id;
        
        /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", example = "角色名称", required = true)
    private String name;
        
        /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述", example = "角色描述", required = true)
    private String description;
        
        
        /**
     * 创建人姓名
     */
    @ApiModelProperty(value = "创建人姓名", example = "创建人姓名", required = true)
    private String createBy;
        
        
        
        /**
     * 修改人姓名
     */
    @ApiModelProperty(value = "修改人姓名", example = "修改人姓名", required = true)
    private String updateBy;
        
        
        /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号", example = "排序号", required = true)
    private Integer sortNumber;
        
        
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = true)
    private String organizationCode;
        
}
