package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysUserAdminAddDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.dto.SysUserAdminUpdateDto;
import com.qy.ntf.bean.entity.Role;
import com.qy.ntf.bean.entity.SysUserAdmin;
import com.qy.ntf.bean.entity.SysUserRole;
import com.qy.ntf.bean.param.UpdateAdminRoleParam;
import com.qy.ntf.dao.RoleDao;
import com.qy.ntf.dao.SysUserAdminDao;
import com.qy.ntf.dao.SysUserRoleDao;
import com.qy.ntf.service.SysUserAdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-31 22:14:55 DESC : 管理员表 service服务实现
 */
@Service("sysUserAdminService")
public class SysUserAdminServiceImpl implements SysUserAdminService {

  @Autowired private SysUserAdminDao sysUserAdminDao;

  @Override
  public BaseMapper<SysUserAdmin> getDao() {
    return sysUserAdminDao;
  }

  @Override
  public IPage<SysUserAdminDto> getListByPage(
      Class<SysUserAdminDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysUserAdmin> queryWrapper) {
    IPage<SysUserAdminDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public void resetPassword(Long userId, String md5) {
    SysUserAdmin sysUserAdmin = sysUserAdminDao.selectById(userId);
    if (sysUserAdmin != null) {
      sysUserAdmin.setAdminPass(md5);
      sysUserAdminDao.updateById(sysUserAdmin);
    } else {
      throw new RuntimeException("无效的id");
    }
  }

  @Override
  public List<SysUserAdmin> selectByAccount(String adminAccount) {
    LambdaQueryWrapper<SysUserAdmin> que = new LambdaQueryWrapper<>();
    que.eq(SysUserAdmin::getAdminAccount, adminAccount);
    return sysUserAdminDao.selectList(que);
  }

  @Autowired private RoleDao roleDao;
  @Autowired private SysUserRoleDao sysUserRoleDao;

  @Override
  public void updateRoles(UpdateAdminRoleParam param) {
    SysUserAdmin sysUserAdmin = sysUserAdminDao.selectById(param.getAdminId());
    if (sysUserAdmin == null) throw new RuntimeException("对不起管理员id异常");
    List<Role> roles = roleDao.selectBatchIds(param.getRoleIds());
    if (roles.size() != param.getRoleIds().size()) throw new RuntimeException("对不起角色id异常");
    LambdaQueryWrapper<SysUserRole> que = new LambdaQueryWrapper<>();
    que.eq(SysUserRole::getUserId, param.getAdminId());
    sysUserRoleDao.delete(que);
    for (Long roleId : param.getRoleIds()) {
      SysUserRole record = new SysUserRole();
      record.setRoleId(roleId);
      record.setUserId(param.getAdminId());
      sysUserRoleDao.insert(record);
    }
  }

  @Override
  public void updateAdminRoleIds(SysUserAdminDto user) {
    LambdaQueryWrapper<SysUserRole> que = new LambdaQueryWrapper<>();
    que.eq(SysUserRole::getUserId, user.getId());
    sysUserRoleDao.delete(que);
    for (Long roleId : user.getRoleIds()) {
      SysUserRole record = new SysUserRole();
      record.setRoleId(roleId);
      record.setUserId(user.getId());
      sysUserRoleDao.insert(record);
    }
  }

  @Override
  public SysUserAdminDto getSysUserAdminById(Long id) {
    Optional<SysUserAdminDto> optional = selectDataById(SysUserAdminDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysUserAdminAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysUserAdminDto dto = new SysUserAdminDto();
    md.map(tmpDto, dto);
    insertData(dto, SysUserAdmin.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysUserAdminUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysUserAdminDto dto = new SysUserAdminDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysUserAdminDto dto) {
    SysUserAdminDto sysUserAdmin = getSysUserAdminById(dto.getId());
    if (sysUserAdmin.getState() == 1) {
      sysUserAdmin.setState(-1);
    } else {
      sysUserAdmin.setState(1);
    }
    sysUserAdmin.setUpdateTime(new Date());
    sysUserAdmin.setUpdateId(dto.getUpdateId());
    updateDataById(sysUserAdmin.getId(), sysUserAdmin);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysUserAdminDto> list(SysUserAdminDto dto) {
    LambdaQueryWrapper<SysUserAdmin> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysUserAdminDto.class, queryWrapper);
  }
}
