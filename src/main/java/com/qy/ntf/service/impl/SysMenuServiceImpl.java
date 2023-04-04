package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysMenuAddDto;
import com.qy.ntf.bean.dto.SysMenuDto;
import com.qy.ntf.bean.dto.SysMenuUpdateDto;
import com.qy.ntf.bean.entity.SysMenu;
import com.qy.ntf.dao.SysMenuDao;
import com.qy.ntf.service.SysMenuService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统权限 service服务实现
 */
@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

  @Autowired private SysMenuDao sysMenuDao;

  @Override
  public BaseMapper<SysMenu> getDao() {
    return sysMenuDao;
  }

  @Override
  public IPage<SysMenuDto> getListByPage(
      Class<SysMenuDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysMenu> queryWrapper) {
    IPage<SysMenuDto> selectPageList = selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysMenuDto getSysMenuById(Long id) {
    Optional<SysMenuDto> optional = selectDataById(SysMenuDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysMenuAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysMenuDto dto = new SysMenuDto();
    md.map(tmpDto, dto);
    insertData(dto, SysMenu.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysMenuUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysMenuDto dto = new SysMenuDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysMenuDto dto) {
    SysMenuDto sysMenu = getSysMenuById(dto.getId());
    if (sysMenu.getState() == 1) {
      sysMenu.setState(2);
    } else {
      sysMenu.setState(1);
    }
    sysMenu.setUpdateTime(new Date());
    sysMenu.setUpdateId(dto.getUpdateId());
    updateDataById(sysMenu.getId(), sysMenu);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysMenuDto> list(SysMenuDto dto) {
    LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysMenuDto.class, queryWrapper);
  }
}
