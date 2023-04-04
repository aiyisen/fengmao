package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysPlateConfigAddDto;
import com.qy.ntf.bean.dto.SysPlateConfigDto;
import com.qy.ntf.bean.dto.SysPlateConfigUpdateDto;
import com.qy.ntf.bean.entity.SysPlateConfig;
import com.qy.ntf.dao.SysPlateConfigDao;
import com.qy.ntf.service.SysPlateConfigService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 平台信息 service服务实现
 */
@Service("sysPlateConfigService")
public class SysPlateConfigServiceImpl implements SysPlateConfigService {

  @Autowired private SysPlateConfigDao sysPlateConfigDao;

  @Override
  public BaseMapper<SysPlateConfig> getDao() {
    return sysPlateConfigDao;
  }

  @Override
  public IPage<SysPlateConfigDto> getListByPage(
      Class<SysPlateConfigDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysPlateConfig> queryWrapper) {
    IPage<SysPlateConfigDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysPlateConfigDto appGetPlate() {
    LambdaQueryWrapper<SysPlateConfig> que = new LambdaQueryWrapper<>();
    que.orderByDesc(BaseEntity::getCreateTime);
    List<SysPlateConfig> sysPlateConfigs = sysPlateConfigDao.selectList(que);
    ModelMapper md = new ModelMapper();
    SysPlateConfigDto result = new SysPlateConfigDto();
    md.map(sysPlateConfigs.get(0), result);
    return result;
  }

  @Override
  public SysPlateConfigDto getSysPlateConfigById(Long id) {
    Optional<SysPlateConfigDto> optional = selectDataById(SysPlateConfigDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysPlateConfigAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysPlateConfigDto dto = new SysPlateConfigDto();
    md.map(tmpDto, dto);
    insertData(dto, SysPlateConfig.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysPlateConfigUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysPlateConfigDto dto = new SysPlateConfigDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysPlateConfigDto dto) {
    SysPlateConfigDto sysPlateConfig = getSysPlateConfigById(dto.getId());
    if (sysPlateConfig.getState() == 1) {
      sysPlateConfig.setState(2);
    } else {
      sysPlateConfig.setState(1);
    }
    sysPlateConfig.setUpdateTime(new Date());
    sysPlateConfig.setUpdateId(dto.getUpdateId());
    updateDataById(sysPlateConfig.getId(), sysPlateConfig);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public SysPlateConfigDto plateInfo() {
    LambdaQueryWrapper<SysPlateConfig> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysPlateConfigDto.class, queryWrapper).get(0);
  }
}
