package com.qy.ntf.bean.dto;

import com.qy.ntf.bean.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel(description = "用户登录后，返回的信息")
@Data
public class UserInfo {

  @ApiModelProperty("用户信息")
  private SysUser user;

  @ApiModelProperty("用户的菜单")
  private List<Map<String, Object>> menusList;

  @ApiModelProperty("用户的按钮")
  private List<Map<String, Object>> buttonList;

  private Integer isBeforeUser;

  private String token;
  private Integer treaOrderCount;
  public String bankCard;
}
