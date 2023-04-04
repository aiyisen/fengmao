package com.qy.ntf.bean.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.util.LLianPayDateUtils;
import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.llPay.H5OpenacctDemo;
import com.qy.ntf.util.llPay.LLianPayClient;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统用户 实体
 */
@TableName("sys_user")
@Data
public class SysUser extends BaseEntity {

  /** 主键 */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 苹果id */
  private String appleId;

  /** 微信id */
  private String wxOpenId;

  /** qqID */
  private String qqOpenId;

  private String headImg;
  /** 账户名 */
  private String username;

  /** 电话 */
  @TableField(value = "phone")
  private String phone;

  /** 电子邮箱 */
  @TableField(value = "email")
  private String email;

  /** 密码 */
  @TableField(value = "pass")
  private String pass;

  private String operationPass;

  /** 区块链地址 */
  private String linkAddress;

  /** 真实姓名 */
  private String realName;

  /** 身份证号 */
  private String idCard;

  /** 身份证正面图片 */
  private String idFirstPath;

  /** 身份证背面图片 */
  private String idSecPath;

  /** 是否实名0未实名1已实名 */
  private String isTrue;

  /** 积分总量 */
  private Long metaCount;

  /** 余额（单位元） */
  private BigDecimal balance;

  /** 用户唯一标识 */
  private String userIndex;

  /** 组织机构 */
  //  private String organizationCode;

  private Integer isVip;

  public Integer getIsOpening() {
    if (this.isOpening != null && this.isOpening == 0) {
      String timestamp = LLianPayDateUtils.getTimestamp();
      JSONObject params = new JSONObject();
      params.put("timestamp", timestamp);
      params.put("oid_partner", ResourcesUtil.getProperty("lianlian_oid"));
      params.put("user_id", phone);
      // 设置商户订单信息

      // 设置付款方信息
      String url = "https://accpapi.lianlianpay.com/v1/acctmgr/query-userinfo";
      LLianPayClient lLianPayClient = new LLianPayClient();
      String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
      JSONObject result = JSONObject.parseObject(resultJsonStr, JSONObject.class);
      if (result.getString("bank_open_flag") != null
          && !result.getString("bank_open_flag").equals("")) {
        if (result.getString("bank_open_flag").equals("1")) {
          return 1;
        } else {
          return 0;
        }
      } else {
        return 0;
      }

    } else {
      return isOpening;
    }
  }

  private Integer isOpening;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date vipEndTime;

  public String publicKey;
  public String privateKey;
  public String mnemonic;
  public String bankCard;

  @TableField(exist = false)
  public Integer treaOrderCount;

  @TableField(exist = false)
  public Integer isBeforeUser;

  @TableField(exist = false)
  public Integer total;

  @TableField(exist = false)
  public Integer hasCount;

  @ApiModelProperty(value = "邀请码")
  @TableField(exist = false)
  private String inviteCode;

  public BigDecimal getBalance() {
    return getBal(this.phone);
  }

  @TableField(exist = false)
  public Integer flag;

  public BigDecimal getBal(String phone) {
    if (flag == null) {
      String timestamp = LLianPayDateUtils.getTimestamp();
      JSONObject params = new JSONObject();
      params.put("timestamp", timestamp);
      params.put("oid_partner", ResourcesUtil.getProperty("lianlian_oid"));
      params.put("user_id", phone);
      params.put("user_type", "INNERUSER");
      // 设置商户订单信息

      // 设置付款方信息
      String url = "https://accpapi.lianlianpay.com/v1/acctmgr/query-acctinfo";
      LLianPayClient lLianPayClient = new LLianPayClient();
      String resultJsonStr = lLianPayClient.sendRequest(url, JSON.toJSONString(params));
      JSONObject result = JSONObject.parseObject(resultJsonStr, JSONObject.class);
      if (result.getString("ret_code").equals("0000")) {
        JSONArray acctinfo_list = result.getJSONArray("acctinfo_list");
        BigDecimal total = new BigDecimal("0");
        for (int i = 0; i < acctinfo_list.size(); i++) {
          JSONObject jsonObject = acctinfo_list.getJSONObject(i);
          if (jsonObject != null) {
            if (jsonObject.getString("acct_type").equals("USEROWN_AVAILABLE")
                || jsonObject.getString("acct_type").equals("USEROWN_PSETTLE")) {
              total = total.add(jsonObject.getBigDecimal("amt_balaval"));
            }
          }
        }
        return total;
      } else {
        H5OpenacctDemo.innerUser(this.phone);
        return BigDecimal.ZERO;
      }
    }
    return BigDecimal.ZERO;
  }
}
