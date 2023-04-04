package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qy.ntf.bean.dto.SysOrgAddDto;
import com.qy.ntf.bean.dto.SysOrgDto;
import com.qy.ntf.bean.dto.SysOrgUpdateDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.entity.SysOrg;
import com.qy.ntf.dao.SysOrgDao;
import com.qy.ntf.service.SysOrgService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-07-05 20:10:31 DESC : 发行方 service服务实现
 */
@Service("sysOrgService")
public class SysOrgServiceImpl implements SysOrgService {

  @Autowired private SysOrgDao sysOrgDao;

  @Override
  public BaseMapper<SysOrg> getDao() {
    return sysOrgDao;
  }

  @Override
  public IPage<SysOrgDto> getListByPage(
      Class<SysOrgDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysOrg> queryWrapper) {
    return selectPageList(clazz, currentPage, pageSize, queryWrapper);
  }

  @Override
  public List<SysOrg> getListByIds(List<Long> orgIds) {
    LambdaQueryWrapper<SysOrg> que = Wrappers.lambdaQuery();
    que.in(SysOrg::getId, orgIds);
    return sysOrgDao.selectList(que);
  }

  @Override
  public SysOrgDto getSysOrgById(Long id) {
    Optional<SysOrgDto> optional = selectDataById(SysOrgDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysOrgAddDto tmpDto, SysUserAdminDto adminUserData) {
    ModelMapper md = new ModelMapper();
    SysOrgDto dto = new SysOrgDto();
    md.map(tmpDto, dto);
    dto.setCreateId(adminUserData.getId());
    dto.setCreateTime(new Date());
    dto.setState(1);
    insertData(dto, SysOrg.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysOrgUpdateDto tmpDto, SysUserAdminDto adminUserData) {
    ModelMapper md = new ModelMapper();
    SysOrgDto dto = new SysOrgDto();
    md.map(tmpDto, dto);
    dto.setUpdateId(adminUserData.getId());
    dto.setUpdateTime(new Date());
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysOrgDto dto) {
    SysOrgDto sysOrg = getSysOrgById(dto.getId());
    if (sysOrg.getState() == 1) {
      sysOrg.setState(2);
    } else {
      sysOrg.setState(1);
    }
    sysOrg.setUpdateTime(new Date());
    sysOrg.setUpdateId(dto.getUpdateId());
    updateDataById(sysOrg.getId(), sysOrg);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    SysOrg sysOrg = sysOrgDao.selectById(id);
    sysOrg.setState(-1);
    sysOrgDao.updateById(sysOrg);
  }

  @Override
  public List<SysOrgDto> list(SysOrgDto dto) {
    LambdaQueryWrapper<SysOrg> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysOrgDto.class, queryWrapper);
  }
}
