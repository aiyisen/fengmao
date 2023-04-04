package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysHelpAddDto;
import com.qy.ntf.bean.dto.SysHelpDto;
import com.qy.ntf.bean.dto.SysHelpUpdateDto;
import com.qy.ntf.bean.entity.SysHelp;
import com.qy.ntf.dao.SysHelpDao;
import com.qy.ntf.service.SysHelpService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 帮助中心 service服务实现
 */
@Service("sysHelpService")
public class SysHelpServiceImpl implements SysHelpService {

  @Autowired private SysHelpDao sysHelpDao;

  @Override
  public BaseMapper<SysHelp> getDao() {
    return sysHelpDao;
  }

  @Override
  public IPage<SysHelpDto> getListByPage(
      Class<SysHelpDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysHelp> queryWrapper) {
    IPage<SysHelpDto> selectPageList = selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysHelpDto getSysHelpById(Long id) {
    Optional<SysHelpDto> optional = selectDataById(SysHelpDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysHelpAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysHelpDto dto = new SysHelpDto();
    md.map(tmpDto, dto);
    insertData(dto, SysHelp.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysHelpUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysHelpDto dto = new SysHelpDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysHelpDto dto) {
    SysHelpDto sysHelp = getSysHelpById(dto.getId());
    if (sysHelp.getState() == 1) {
      sysHelp.setState(2);
    } else {
      sysHelp.setState(1);
    }
    sysHelp.setUpdateTime(new Date());
    sysHelp.setUpdateId(dto.getUpdateId());
    updateDataById(sysHelp.getId(), sysHelp);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysHelpDto> list() {
    LambdaQueryWrapper<SysHelp> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SysHelp::getState, 1);
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    return selectList(SysHelpDto.class, queryWrapper);
  }
}
