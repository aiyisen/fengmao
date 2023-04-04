package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 王振读 email Created on 2022-05-25 19:27:16 DESC : 系统用户 查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统用户 查询参数", description = "系统用户 查询参数")
public class SysUserSelectDto extends BaseEntity {

  /** 主键 */
  @ApiModelProperty(value = "主键", example = "主键", required = false)
  private Long id;

  /** 苹果id */
  @ApiModelProperty(value = "苹果id", example = "苹果id", required = false)
  private String appleId;

  /** 微信id */
  @ApiModelProperty(value = "微信id", example = "微信id", required = false)
  private String wxOpenId;

  /** qqID */
  @ApiModelProperty(value = "qqID", example = "qqID", required = false)
  private String qqOpenId;

  /** 账户名 */
  @ApiModelProperty(value = "账户名", example = "账户名", required = false)
  private String username;

  /** 电话 */
  @ApiModelProperty(value = "电话", example = "电话", required = false)
  private String phone;

  /** 电子邮箱 */
  @ApiModelProperty(value = "电子邮箱", example = "电子邮箱", required = false)
  private String email;

  /** 密码 */
  @ApiModelProperty(value = "密码", example = "密码", required = false)
  private String pass;

  /** 区块链地址 */
  @ApiModelProperty(value = "区块链地址", example = "区块链地址", required = false)
  private String linkAddress;

  /** 真实姓名 */
  @ApiModelProperty(value = "真实姓名", example = "真实姓名", required = false)
  private String realName;

  /** 身份证号 */
  @ApiModelProperty(value = "身份证号", example = "身份证号", required = false)
  private String idCard;

  /** 身份证正面图片 */
  @ApiModelProperty(value = "身份证正面图片", example = "身份证正面图片", required = false)
  private String idFirstPath;

  /** 身份证背面图片 */
  @ApiModelProperty(value = "身份证背面图片", example = "身份证背面图片", required = false)
  private String idSecPath;

  /** 是否实名0未实名1已实名 */
  @ApiModelProperty(value = "是否实名0未实名1已实名", example = "是否实名0未实名1已实名", required = false)
  private String isTrue;

  /** 积分总量 */
  @ApiModelProperty(value = "积分总量", example = "积分总量", required = false)
  private Long metaCountq;

  /** 余额（单位元） */
  @ApiModelProperty(value = "余额（单位元）", example = "余额（单位元）", required = false)
  private BigDecimal balance;

  /** 用户唯一标识 */
  @ApiModelProperty(value = "用户唯一标识", example = "用户唯一标识", required = false)
  private String userIndex;

  /** 组织机构 */
  @ApiModelProperty(value = "组织机构", example = "组织机构", required = false)
  private String organizationCode;
}
