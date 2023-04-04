package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description = "菜单")
@Data
public class MenuDto extends BaseEntity {
	
	@ApiModelProperty("id")
	private Long    id;
	
	@ApiModelProperty("pid")
	private Long    pid;
	
	@ApiModelProperty("类型 - 1:目录 2:菜单 3:按钮")
	private Integer type;
	
	@ApiModelProperty("菜单名称")
	private String  name;
	
	@ApiModelProperty("菜单路由")
	private String  path;
	
	@ApiModelProperty("前端菜单图标名称")
	private String  icon;
	
	@ApiModelProperty("描述")
	private String  description;
	
	@ApiModelProperty("排序号")
	private Integer sortNumber;
	
	@ApiModelProperty("状态")
	private Integer state;
	
	@ApiModelProperty("子菜单")
	private List<MenuDto> children = null;
	
	@Override
	public String toString() {
		return id + ", " + name + ", type=" + type + ", pid=" + pid; 
	}
}
