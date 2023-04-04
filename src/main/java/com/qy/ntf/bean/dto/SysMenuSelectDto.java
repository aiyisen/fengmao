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
 * DESC : 系统权限 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统权限 查询参数", description = "系统权限 查询参数")
public class SysMenuSelectDto extends BaseEntity {

    
        /**
     * 主键
     */
    @ApiModelProperty(value = "主键", example = "主键", required = false)
    private Integer id;
        
        /**
     * 上级主键
     */
    @ApiModelProperty(value = "上级主键", example = "上级主键", required = false)
    private Integer pid;
        
        /**
     * 权限类型(1:目录 2:菜单 3:按钮)
     */
    @ApiModelProperty(value = "权限类型(1:目录 2:菜单 3:按钮)", example = "权限类型(1:目录 2:菜单 3:按钮)", required = false)
    private Long type;
        
        /**
     * 权限名
     */
    @ApiModelProperty(value = "权限名", example = "权限名", required = false)
    private String name;
        
        /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识", example = "权限标识", required = false)
    private String identifying;
        
        /**
     * 权限地址
     */
    @ApiModelProperty(value = "权限地址", example = "权限地址", required = false)
    private String path;
        
        /**
     * 权限图标
     */
    @ApiModelProperty(value = "权限图标", example = "权限图标", required = false)
    private String icon;
        
        /**
     * 权限描述
     */
    @ApiModelProperty(value = "权限描述", example = "权限描述", required = false)
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
    private Long sortNumber;
        
        
}
