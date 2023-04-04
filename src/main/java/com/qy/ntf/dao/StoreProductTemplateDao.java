package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.dto.StoreProductDto;
import com.qy.ntf.bean.entity.StoreProductTemplate;
import com.qy.ntf.dao.provider.StoreProductTemplateProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 特权商品材料模板 Dao
 */
@Mapper
public interface StoreProductTemplateDao extends BaseMapper<StoreProductTemplate> {
  @Select(
      "select store_product_template.* from store_product_template where store_product_template.productId = #{id}")
  List<StoreProductTemplate> getNeedTreasure(@Param("id") Long id);

  @Delete("delete from store_product_template where productId = #{productId}")
  void deleteLink(@Param("productId") Long id);

  @SelectProvider(type = StoreProductTemplateProvider.class, method = "selectNeedCount")
  List<StoreProductDto> selectNeedCount(List<Long> collect);
}
