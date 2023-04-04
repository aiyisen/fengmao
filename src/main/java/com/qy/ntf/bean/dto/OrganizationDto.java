package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description = "组织机构")
@Data
public class OrganizationDto extends BaseEntity {
	
	@ApiModelProperty("id")
	private Long    id;
	
	@ApiModelProperty("组织机构代码")
	private String code;
	
	@ApiModelProperty("组织机构名称")
	private String  name;
	
	@ApiModelProperty("组织机构描述")
	private String  description;
	
	@ApiModelProperty("下级组织机构")
	private List<OrganizationDto> children = null;
	
	@ApiModelProperty("逻辑删除(0:失效 1:有效)")
	private Integer state;

}
