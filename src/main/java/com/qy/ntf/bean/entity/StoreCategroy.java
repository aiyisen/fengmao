package com.qy.ntf.bean.entity;

import com.qy.ntf.base.BaseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

    import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 王振读
 * 2022-08-06 13:01:44
 * DESC : 藏品分类 实体
 */
@TableName("store_categroy")
@Data
public class StoreCategroy extends BaseEntity{



/**
 * 
 */
        @TableField(value = "id")
private Long id;

/**
 * 分类名称
 */
    @TableField(value = "cate_name")
private String cateName;





}
