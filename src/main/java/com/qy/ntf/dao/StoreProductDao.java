package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.StoreProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 权益商城-普通专区/特权专区/兑换专区 主体 Dao
 */
@Mapper
public interface StoreProductDao extends BaseMapper<StoreProduct> {}
