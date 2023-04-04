package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 平台信息 实体
 */
@TableName("sys_plate_config")
@Data
public class SysPlateConfig extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户协议（富文本） */
  private String userAgreement;

  /** 隐私协议（富文本） */
  private String privacyPolicy;

  /** 运营商协议（富文本） */
  private String mobilePolicy;

  /** 新手指南（富文本） */
  private String newbieGuide;

  /** 关于我们（富文本） */
  private String aboutUs;

  private String vipRules;

  private String vipEquity;

  private String inviteInfo;

  private String treaNeedKnow;

  private String beforeNeedKnow;

  private String beforeRule;

  private String outPolicy;
  private String saleNeedKonw;
}
