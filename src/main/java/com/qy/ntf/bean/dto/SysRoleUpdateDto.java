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
 * DESC : 系统角色 修改参数
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统角色 修改参数", description = "系统角色 修改参数")
public class SysRoleUpdateDto extends BaseEntity {

    
        /**
     * 主键
     */
    @ApiModelProperty(value = "主键", example = "主键", required = false)
    private Integer id;
        
        /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", example = "角色名称", required = false)
    private String name;
        
        /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述", example = "角色描述", required = false)
    private String description;
        
        
        /**
     * 创建人姓名
     */
    @ApiModelProperty(value = "创建人姓名", example = "创建人姓名", required = false)
    private String createBy;
        
        
        
        /**
     * 修改人姓名
     */
    @ApiModelProperty(value = "修改人姓名", example = "修改人姓名", required = false)
    private String updateBy;
        
        
        /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号", example = "排序号", required = false)
    private Integer sortNumber;
        
        
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private String organizationCode;
        }
