package com.qy.ntf.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qy.ntf.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王振读 2022-07-31 20:17:27 DESC : ios价格 实体
 */
@TableName("store_tre_ios_price")
@Data
public class StoreTreIosPrice extends BaseEntity {

  /** */
  @TableField(value = "id")
  private Long id;

  /** ios价格标题 */
  @TableField(value = "price_title")
  private String priceTitle;

  /** ios价格内容 */
  @TableField(value = "price_content")
  private String priceContent;

  /** ios价格 */
  @TableField(value = "price")
  private BigDecimal price;
}
