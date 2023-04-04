package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysRoleAddDto;
import com.qy.ntf.bean.dto.SysRoleDto;
import com.qy.ntf.bean.dto.SysRoleUpdateDto;
import com.qy.ntf.bean.entity.SysRole;
import com.qy.ntf.dao.SysRoleDao;
import com.qy.ntf.service.SysRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统角色 service服务实现
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

  @Autowired private SysRoleDao sysRoleDao;

  @Override
  public BaseMapper<SysRole> getDao() {
    return sysRoleDao;
  }

  @Override
  public IPage<SysRoleDto> getListByPage(
      Class<SysRoleDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysRole> queryWrapper) {
    IPage<SysRoleDto> selectPageList = selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysRoleDto getSysRoleById(Long id) {
    Optional<SysRoleDto> optional = selectDataById(SysRoleDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysRoleAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysRoleDto dto = new SysRoleDto();
    md.map(tmpDto, dto);
    insertData(dto, SysRole.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysRoleUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysRoleDto dto = new SysRoleDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysRoleDto dto) {
    SysRoleDto sysRole = getSysRoleById(dto.getId());
    if (sysRole.getState() == 1) {
      sysRole.setState(2);
    } else {
      sysRole.setState(1);
    }
    sysRole.setUpdateTime(new Date());
    sysRole.setUpdateId(dto.getUpdateId());
    updateDataById(sysRole.getId(), sysRole);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysRoleDto> list(SysRoleDto dto) {
    LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysRoleDto.class, queryWrapper);
  }
}
