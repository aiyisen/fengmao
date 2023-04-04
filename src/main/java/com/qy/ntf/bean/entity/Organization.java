package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

@TableName("sys_organization")
@Data
public class Organization extends BaseEntity {
	
	@TableId(type = IdType.AUTO)
	private Long  id;
	private String code;
	private String  name;
	private String  description;
	
}
