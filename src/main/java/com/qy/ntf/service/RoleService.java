package com.qy.ntf.service;

import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.RoleDto;
import com.qy.ntf.bean.entity.Role;
import com.qy.ntf.dao.RoleDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService implements BaseService<RoleDto,Role> {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public RoleDao getDao() {
		return this.roleDao;
	}
	
	public void deleteRoleById(Long id) throws Exception {
		Integer count = roleDao.getMenuCount(id);
		if(count > 0) throw new Exception("该角色下有菜单数据，不能删除");
		this.deleteDataById(id);
	}
	
	@Transactional
	public void updateUserRoles(Long userId, List<Long> roleIds) {
		roleDao.deleteUserRole(userId);
		roleIds.stream().forEach(roleId -> roleDao.insertUserRole(userId, roleId));
	}
	
	// 获取用户拥有的角色
	public List<RoleDto> getUserRoles(Long userId) {
		ModelMapper modelMapper = new ModelMapper();
		List<Role> roles = roleDao.getUserRoles(userId);
		List<RoleDto> list = roles.stream().map(role -> modelMapper.map(role, RoleDto.class))
				.collect(Collectors.toList());
		return list;
	}

	public void updateState(Long id, Integer state) {
		roleDao.updateState(id, state);
	}
	
}
