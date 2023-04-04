package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统用户 添加参数
 */
@Data
@ApiModel(value = "系统用户 添加参数", description = "系统用户 添加参数")
public class SysUserAddDto extends BaseEntity {

  /** 主键 */
  @ApiModelProperty(value = "主键", example = "主键", required = true)
  private Long id;

  /** 苹果id */
  @ApiModelProperty(value = "苹果id", example = "苹果id", required = true)
  private String appleId;

  /** 微信id */
  @ApiModelProperty(value = "微信id", example = "微信id", required = true)
  private String wxOpenId;

  /** qqID */
  @ApiModelProperty(value = "qqID", example = "qqID", required = true)
  private String qqOpenId;

  /** 账户名 */
  @ApiModelProperty(value = "账户名", example = "账户名", required = true)
  private String username;

  /** 电话 */
  @ApiModelProperty(value = "电话", example = "电话", required = true)
  private String phone;

  /** 电子邮箱 */
  @ApiModelProperty(value = "电子邮箱", example = "电子邮箱", required = true)
  private String email;

  /** 密码 */
  @ApiModelProperty(value = "密码", example = "密码", required = true)
  private String pass;

  /** 区块链地址 */
  @ApiModelProperty(value = "区块链地址", example = "区块链地址", required = true)
  private String linkAddress;

  /** 真实姓名 */
  @ApiModelProperty(value = "真实姓名", example = "真实姓名", required = true)
  private String realName;

  /** 身份证号 */
  @ApiModelProperty(value = "身份证号", example = "身份证号", required = true)
  private String idCard;

  /** 身份证正面图片 */
  @ApiModelProperty(value = "身份证正面图片", example = "身份证正面图片", required = true)
  private String idFirstPath;

  /** 身份证背面图片 */
  @ApiModelProperty(value = "身份证背面图片", example = "身份证背面图片", required = true)
  private String idSecPath;

  /** 是否实名0未实名1已实名 */
  @ApiModelProperty(value = "是否实名0未实名1已实名", example = "是否实名0未实名1已实名", required = true)
  private String isTrue;

  /** 积分总量 */
  @ApiModelProperty(value = "积分总量", example = "积分总量", required = true)
  private Long metaCountq;

  /** 余额（单位元） */
  @ApiModelProperty(value = "余额（单位元）", example = "余额（单位元）", required = true)
  private BigDecimal balance;

  /** 用户唯一标识 */
  @ApiModelProperty(value = "用户唯一标识", example = "用户唯一标识", required = true)
  private String userIndex;

  /** 组织机构 */
  @ApiModelProperty(value = "组织机构", example = "组织机构", required = true)
  private String organizationCode;
}
