package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读
 * 2022-05-25 19:27:16
 * DESC : 角色-菜单 添加参数
 */
@Data
@ApiModel(value = "角色-菜单 添加参数", description = "角色-菜单 添加参数")
public class SysRoleMenuAddDto extends BaseEntity {


    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = true)
    private Integer id;
        
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = true)
    private Integer roleId;
        
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = true)
    private Integer menuId;
        
}
