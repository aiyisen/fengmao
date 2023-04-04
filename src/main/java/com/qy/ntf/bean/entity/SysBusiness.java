package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 商务合作记录 实体
 */
@TableName("sys_business")
@Data
public class SysBusiness extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 姓名 */
  private String userName;

  /** 企业 */
  private String orgName;

  /** 联系人职务 */
  private String contactJob;

  /** 联系方式 */
  private String tell;

  /** 电子邮箱 */
  private String eMail;

  /** 微信号 */
  private String wxCode;

  /** 拟合作ip信息 */
  private String ipTitle;

  /** 您与合作ip关系 */
  private String ipRelation;

  /** 从何处获取的入驻申请链接 */
  private String fromWhere;
}
