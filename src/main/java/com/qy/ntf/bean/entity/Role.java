package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;

@TableName("sys_role")
public class Role extends BaseEntity {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String name;
  private String description;
  private Integer sortNumber;
  private Integer state;
  //	private String  organizationCode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getSortNumber() {
    return sortNumber;
  }

  public void setSortNumber(Integer sortNumber) {
    this.sortNumber = sortNumber;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
  //	public String getOrganizationCode() {
  //		return organizationCode;
  //	}
  //	public void setOrganizationCode(String organizationCode) {
  //		this.organizationCode = organizationCode;
  //	}

}
