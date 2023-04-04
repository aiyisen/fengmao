package com.qy.ntf.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "角色按钮")
public class ButtonPermissionsDto {

	@ApiModelProperty("bame")
	private String bame;
	
	@ApiModelProperty("bpath")
	private String bpath;

	@ApiModelProperty("mpath")
	private String mpath;

	@ApiModelProperty("mname")
	private String mname;

	public String getBame() {
		return bame;
	}

	public void setBame(String bame) {
		this.bame = bame;
	}

	public String getBpath() {
		return bpath;
	}

	public void setBpath(String bpath) {
		this.bpath = bpath;
	}

	public String getMpath() {
		return mpath;
	}

	public void setMpath(String mpath) {
		this.mpath = mpath;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}
	
	
	
}
