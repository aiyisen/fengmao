package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qy.ntf.bean.dto.SysSeriesAddDto;
import com.qy.ntf.bean.dto.SysSeriesDto;
import com.qy.ntf.bean.dto.SysSeriesUpdateDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.bean.entity.SysOrg;
import com.qy.ntf.bean.entity.SysSeries;
import com.qy.ntf.dao.StoreTreasureDao;
import com.qy.ntf.dao.SysOrgDao;
import com.qy.ntf.dao.SysSeriesDao;
import com.qy.ntf.service.SysSeriesService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-07-15 19:26:43 DESC : 系列主体 service服务实现
 */
@Service("sysSeriesService")
public class SysSeriesServiceImpl implements SysSeriesService {

  @Autowired private SysSeriesDao sysSeriesDao;
  @Autowired private StoreTreasureDao storeTreasureDao;

  @Override
  public BaseMapper<SysSeries> getDao() {
    return sysSeriesDao;
  }

  @Override
  public IPage<SysSeriesDto> getListByPage(
      Class<SysSeriesDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysSeries> queryWrapper) {
    IPage<SysSeriesDto> selectPageList = selectPageList(clazz, currentPage, pageSize, queryWrapper);
    LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery(StoreTreasure.class);
    List<Long> collect =
        selectPageList.getRecords().stream().map(SysSeriesDto::getId).collect(Collectors.toList());
    collect.add(-11111L);
    que.in(StoreTreasure::getSysSeriesId, collect);
    List<StoreTreasure> storeTreasures = storeTreasureDao.selectList(que);
    for (SysSeriesDto record : selectPageList.getRecords()) {

      if (record.getStarttime() != null) {
        if (record.getStarttime().getTime() > System.currentTimeMillis()) {
          record.setFlag(0);
        } else {
          int surpless =
              storeTreasures.stream()
                  .filter(o -> o.getSysSeriesId().equals(record.getId()))
                  .mapToInt(StoreTreasure::getSurplusCount)
                  .sum();
          if (surpless == 0) {
            record.setFlag(2);
          } else {
            record.setFlag(1);
          }
        }
      } else {
        record.setFlag(0);
      }
    }
    Set<Long> orgIds =
        selectPageList.getRecords().stream()
            .map(SysSeriesDto::getSysorgid)
            .collect(Collectors.toSet());
    if (orgIds.size() > 0) {

      LambdaQueryWrapper<SysOrg> q = Wrappers.lambdaQuery(SysOrg.class);
      q.in(SysOrg::getId, orgIds);
      Map<Long, SysOrg> map =
          sysOrgDao.selectList(q).stream().collect(Collectors.toMap(SysOrg::getId, o -> o));
      selectPageList.getRecords().forEach(o -> o.setSysOrg(map.get(o.getSysorgid())));
    }
    return selectPageList;
  }

  @Autowired private SysOrgDao sysOrgDao;

  @Override
  public SysSeriesDto getSysSeriesById(Long id) {
    Optional<SysSeriesDto> optional = selectDataById(SysSeriesDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysSeriesAddDto tmpDto, SysUserAdminDto adminUserData) {
    ModelMapper md = new ModelMapper();
    md.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    SysSeriesDto dto = new SysSeriesDto();
    md.map(tmpDto, dto);
    dto.setCreateTime(new Date());
    dto.setCreateId(adminUserData.getCreateId());
    dto.setState(2);
    insertData(dto, SysSeries.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysSeriesUpdateDto tmpDto, SysUserAdminDto adminUserData) {
    ModelMapper md = new ModelMapper();
    md.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    SysSeriesDto dto = new SysSeriesDto();
    md.map(tmpDto, dto);
    dto.setUpdateId(adminUserData.getId());
    dto.setUpdateTime(new Date());
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysSeriesDto dto, SysUserAdminDto adminUserData) {
    SysSeriesDto sysSeries = getSysSeriesById(dto.getId());
    if (sysSeries.getState() == 1) {
      sysSeries.setState(2);
    } else {
      sysSeries.setState(1);
    }
    sysSeries.setUpdateTime(new Date());
    sysSeries.setUpdateId(adminUserData.getId());
    updateDataById(sysSeries.getId(), sysSeries);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id, SysUserAdminDto adminUserData) {
    deleteDataById(id);
    LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery(StoreTreasure.class);
    que.eq(StoreTreasure::getSysSeriesId, id);
    List<StoreTreasure> storeTreasures = storeTreasureDao.selectList(que);
    for (StoreTreasure storeTreasure : storeTreasures) {
      storeTreasure.setState(-1);
      storeTreasure.setUpdateTime(new Date());
      storeTreasureDao.updateById(storeTreasure);
    }
  }

  @Override
  public List<SysSeriesDto> list(SysSeriesDto dto) {
    LambdaQueryWrapper<SysSeries> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysSeriesDto.class, queryWrapper);
  }
}
