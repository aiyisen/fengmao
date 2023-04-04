package com.qy.ntf.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(description = "用户信息")
@Data
public class UserDto extends BaseEntity {

  /** 主键 */
  @ApiModelProperty(value = "主键", example = "主键", required = true)
  private Long id;

  /** 苹果id */
  @ApiModelProperty(value = "苹果id", example = "苹果id", required = true)
  private String appleId;

  private String headImg;

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
  private Long metaCount;

  /** 余额（单位元） */
  @ApiModelProperty(value = "余额（单位元）", example = "余额（单位元）", required = true)
  private BigDecimal balance;

  /** 用户唯一标识 */
  @ApiModelProperty(value = "用户唯一标识", example = "用户唯一标识", required = true)
  private String userIndex;

  /** 组织机构 */
  @ApiModelProperty(value = "组织机构", example = "组织机构", required = true)
  private String organizationCode;

  @ApiModelProperty("是否为vip0否1是")
  private Integer isVip;

  @ApiModelProperty("会员截止时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date vipEndTime;

  private Integer ruleCount;
  private Integer hasCount;
  public String bankCard;

  private Long beId;
}
