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
 * DESC : 角色-菜单 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "角色-菜单 查询参数", description = "角色-菜单 查询参数")
public class SysRoleMenuSelectDto extends BaseEntity {

    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Integer id;
        
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Integer roleId;
        
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = false)
    private Integer menuId;
        
}
