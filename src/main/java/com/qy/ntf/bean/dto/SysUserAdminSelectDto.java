package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王振读 email Created on 2022-05-31 22:14:55 DESC : 管理员表 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "管理员表 查询参数", description = "管理员表 查询参数")
public class SysUserAdminSelectDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = false)
  private Long id;

  /** 管理员名称 */
  @ApiModelProperty(value = "管理员名称", example = "管理员名称", required = false)
  private String adminName;

  /** 管理员手机号 */
  @ApiModelProperty(value = "管理员手机号", example = "管理员手机号", required = false)
  private String adminMobile;

  /** 管理员账号 */
  @ApiModelProperty(value = "管理员账号", example = "管理员账号", required = false)
  private String adminAccount;

  /** 管理员密码 */
  @ApiModelProperty(value = "管理员密码", example = "管理员密码", required = false)
  private String adminPass;

  /** 组织机构 */
  @ApiModelProperty(value = "组织机构", example = "组织机构", required = false)
  private String organizationCode;
}
