package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池主体 实体
 */
@TableName("store_pro_pool")
@Data
public class StoreProPools extends BaseEntity {

  /** */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 聚合池商品标题 */
  private String proPoolTitle;

  /** 首图地址 */
  private String indexPath;

  /** 企业名称 */
  private String orgName;

  /** 企业头像 */
  private String orgIndexPath;

  /** 价格 */
  private BigDecimal proPrice;

  /** 标签";"分割 */
  private String tagCon;

  /** 藏品编号 */
  private String proNum;
  /** 来源0平台1用户卖出 */
  private Integer fromType;
  /** 订单指纹 */
  private String orderFingerprint;

  private String headImgPath;

  private Integer headType;
  /** 是否卖出 */
  private Integer isSale;

  private Integer isCompound;
  private Long sysOrgId;
}
