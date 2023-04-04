package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysAreaAddDto;
import com.qy.ntf.bean.dto.SysAreaDto;
import com.qy.ntf.bean.dto.SysAreaUpdateDto;
import com.qy.ntf.bean.entity.SysArea;
import com.qy.ntf.dao.SysAreaDao;
import com.qy.ntf.service.SysAreaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 全国区域信息表 service服务实现
 */
@Service("sysAreaService")
public class SysAreaServiceImpl implements SysAreaService {

  @Autowired private SysAreaDao sysAreaDao;

  @Override
  public BaseMapper<SysArea> getDao() {
    return sysAreaDao;
  }

  @Override
  public IPage<SysAreaDto> getListByPage(
      Class<SysAreaDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysArea> queryWrapper) {
    IPage<SysAreaDto> selectPageList = selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public List<SysArea> getAll() {
    return sysAreaDao.selectList(new LambdaQueryWrapper<>());
  }

  @Override
  public SysAreaDto getSysAreaById(Long id) {
    Optional<SysAreaDto> optional = selectDataById(SysAreaDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysAreaAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysAreaDto dto = new SysAreaDto();
    md.map(tmpDto, dto);
    insertData(dto, SysArea.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysAreaUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysAreaDto dto = new SysAreaDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysAreaDto dto) {
    SysAreaDto sysArea = getSysAreaById(dto.getId());
    if (sysArea.getState() == 1) {
      sysArea.setState(2);
    } else {
      sysArea.setState(1);
    }
    sysArea.setUpdateTime(new Date());
    sysArea.setUpdateId(dto.getUpdateId());
    updateDataById(sysArea.getId(), sysArea);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysAreaDto> list(SysAreaDto dto) {
    LambdaQueryWrapper<SysArea> queryWrapper = new LambdaQueryWrapper<>();
    if (dto.getPid() != null) {
      queryWrapper.eq(SysArea::getPid, dto.getPid());
    }
    return selectList(SysAreaDto.class, queryWrapper);
  }
}
