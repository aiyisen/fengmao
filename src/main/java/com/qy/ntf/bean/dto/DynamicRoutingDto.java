package com.qy.ntf.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "动态路由")
public class DynamicRoutingDto {

	@ApiModelProperty("name")
	private String name;

	@ApiModelProperty("path")
	private String path;

	@ApiModelProperty("icon")
	private String icon;

	@ApiModelProperty("父级名称")
	private String pname;
	
	@ApiModelProperty("父级path")
	private String ppath;
	
	@ApiModelProperty("父级icon")
	private String picon;
	
	private Integer id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPpath() {
		return ppath;
	}

	public void setPpath(String ppath) {
		this.ppath = ppath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPicon() {
		return picon;
	}

	public void setPicon(String picon) {
		this.picon = picon;
	}
	
	
	
}
