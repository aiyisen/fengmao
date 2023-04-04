package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.SysAddress;
import com.qy.ntf.dao.provider.SysAddressPro;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author 王振读 email 2022-05-25 19:27:16 DESC : 收货地址 Dao
 */
@Mapper
public interface SysAddressDao extends BaseMapper<SysAddress> {
  @SelectProvider(method = "selectDetailByIds", type = SysAddressPro.class)
  List<SysAddress> selectDetailByIds(List<Long> collect);
}
