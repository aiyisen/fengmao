package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读
 * 2022-05-25 19:27:16
 * DESC : 用户角色表 添加参数
 */
@Data
@ApiModel(value = "用户角色表 添加参数", description = "用户角色表 添加参数")
public class SysUserRoleAddDto extends BaseEntity {


    
        /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号", example = "用户编号", required = true)
    private Integer userId;
        
        /**
     * 角色编号
     */
    @ApiModelProperty(value = "角色编号", example = "角色编号", required = true)
    private Long roleId;
        
}
