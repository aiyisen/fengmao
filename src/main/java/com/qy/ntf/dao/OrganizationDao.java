package com.qy.ntf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrganizationDao extends BaseMapper<Organization> {
	
	// 获取组织机构的当前级的最大编码
	
	@Select("select max(code) as code from sys_organization where code like concat(#{parent}, '%') and length(code) = #{length}")
	Organization getMaxCode(@Param("parent") String parent, @Param("length") int length);

	//LIKE CONCAT('%',#{ew.entity.name},'%')
	
	@Update("update sys_organization set state = ${state} where id = ${id}")
	void updateState(@Param("id") Long id, @Param("state") Integer state);
	
}