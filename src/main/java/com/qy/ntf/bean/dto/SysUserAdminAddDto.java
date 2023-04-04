package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读 2022-05-31 22:14:55 DESC : 管理员表 添加参数
 */
@Data
@ApiModel(value = "管理员表 添加参数", description = "管理员表 添加参数")
public class SysUserAdminAddDto extends BaseEntity {

  /** */
  @ApiModelProperty(value = "", example = "", required = true)
  private Long id;

  /** 管理员名称 */
  @ApiModelProperty(value = "管理员名称", example = "管理员名称", required = true)
  private String adminName;

  /** 管理员手机号 */
  @ApiModelProperty(value = "管理员手机号", example = "管理员手机号", required = true)
  private String adminMobile;

  /** 管理员账号 */
  @ApiModelProperty(value = "管理员账号", example = "管理员账号", required = true)
  private String adminAccount;

  /** 管理员密码 */
  @ApiModelProperty(value = "管理员密码", example = "管理员密码", required = true)
  private String adminPass;

  /** 组织机构 */
  @ApiModelProperty(value = "组织机构", example = "组织机构", required = true)
  private String organizationCode;
}
