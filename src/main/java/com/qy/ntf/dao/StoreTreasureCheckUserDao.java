package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.entity.StoreTreasureCheckUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 超亲申购中奖名单 Dao
 */
@Mapper
public interface StoreTreasureCheckUserDao extends BaseMapper<StoreTreasureCheckUser> {
  @Select(
      "select sys_user.phone "
          + "from  store_treasure_check_user left join sys_user on sys_user.id = store_treasure_check_user.uId "
          + "where store_treasure_check_user.storeTreasureId = #{id} and store_treasure_check_user.state =1")
  IPage<String> getCheckedUserPhone(Page page, @Param("id") Long id);

  @Select(
      "select * from store_treasure_check_user where storeTreasureId = #{teaPoId} and uId=#{userId} limit 1")
  StoreTreasureCheckUser selectByIdAndUid(
      @Param("userId") Long userId, @Param("teaPoId") Long teaPoId);

  @Select("select * from store_treasure_check_user where storeTreasureId = #{teaPoId} ")
  List<StoreTreasureCheckUser> selectByTreaId(@Param("teaPoId") Long treasureId);
}
