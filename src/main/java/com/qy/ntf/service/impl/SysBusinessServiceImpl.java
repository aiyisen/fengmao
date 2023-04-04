package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysBusinessAddDto;
import com.qy.ntf.bean.dto.SysBusinessDto;
import com.qy.ntf.bean.dto.SysBusinessUpdateDto;
import com.qy.ntf.bean.entity.SysBusiness;
import com.qy.ntf.dao.SysBusinessDao;
import com.qy.ntf.service.SysBusinessService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 商务合作记录 service服务实现
 */
@Service("sysBusinessService")
public class SysBusinessServiceImpl implements SysBusinessService {

  @Autowired private SysBusinessDao sysBusinessDao;

  @Override
  public BaseMapper<SysBusiness> getDao() {
    return sysBusinessDao;
  }

  @Override
  public IPage<SysBusinessDto> getListByPage(
      Class<SysBusinessDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysBusiness> queryWrapper) {
    IPage<SysBusinessDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysBusinessDto getSysBusinessById(Long id) {
    Optional<SysBusinessDto> optional = selectDataById(SysBusinessDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysBusinessAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysBusinessDto dto = new SysBusinessDto();
    md.map(tmpDto, dto);
    insertData(dto, SysBusiness.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysBusinessUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysBusinessDto dto = new SysBusinessDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysBusinessDto dto) {
    SysBusinessDto sysBusiness = getSysBusinessById(dto.getId());
    if (sysBusiness.getState() == 1) {
      sysBusiness.setState(2);
    } else {
      sysBusiness.setState(1);
    }
    sysBusiness.setUpdateTime(new Date());
    sysBusiness.setUpdateId(dto.getUpdateId());
    updateDataById(sysBusiness.getId(), sysBusiness);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysBusinessDto> list(SysBusinessDto dto) {
    LambdaQueryWrapper<SysBusiness> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysBusinessDto.class, queryWrapper);
  }
}
