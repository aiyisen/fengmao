package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysRoleMenuAddDto;
import com.qy.ntf.bean.dto.SysRoleMenuDto;
import com.qy.ntf.bean.dto.SysRoleMenuUpdateDto;
import com.qy.ntf.bean.entity.SysRoleMenu;
import com.qy.ntf.dao.SysRoleMenuDao;
import com.qy.ntf.service.SysRoleMenuService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 角色-菜单 service服务实现
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

  @Autowired private SysRoleMenuDao sysRoleMenuDao;

  @Override
  public BaseMapper<SysRoleMenu> getDao() {
    return sysRoleMenuDao;
  }

  @Override
  public IPage<SysRoleMenuDto> getListByPage(
      Class<SysRoleMenuDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysRoleMenu> queryWrapper) {
    IPage<SysRoleMenuDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysRoleMenuDto getSysRoleMenuById(Long id) {
    Optional<SysRoleMenuDto> optional = selectDataById(SysRoleMenuDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysRoleMenuAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysRoleMenuDto dto = new SysRoleMenuDto();
    md.map(tmpDto, dto);
    insertData(dto, SysRoleMenu.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysRoleMenuUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysRoleMenuDto dto = new SysRoleMenuDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysRoleMenuDto dto) {
    SysRoleMenuDto sysRoleMenu = getSysRoleMenuById(dto.getId());
    if (sysRoleMenu.getState() == 1) {
      sysRoleMenu.setState(2);
    } else {
      sysRoleMenu.setState(1);
    }
    sysRoleMenu.setUpdateTime(new Date());
    sysRoleMenu.setUpdateId(dto.getUpdateId());
    updateDataById(sysRoleMenu.getId(), sysRoleMenu);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysRoleMenuDto> list(SysRoleMenuDto dto) {
    LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysRoleMenuDto.class, queryWrapper);
  }
}
