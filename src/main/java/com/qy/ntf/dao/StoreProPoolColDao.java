package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.dto.StoreProPoolDto;
import com.qy.ntf.bean.entity.StoreProPoolCol;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 聚合池藏品收藏 Dao
 */
@Mapper
public interface StoreProPoolColDao extends BaseMapper<StoreProPoolCol> {
  @Select(
      "select * from store_pro_pool where id in (select store_pro_pool_col.proPoolId from store_pro_pool_col where store_pro_pool_col.uId=#{userId})")
  IPage<StoreProPoolDto> getListByPageAndUserId(Page storeProPoolPage, @Param("userId") Long id);
}
