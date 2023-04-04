package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysUserBackpackAddDto;
import com.qy.ntf.bean.dto.SysUserBackpackDto;
import com.qy.ntf.bean.dto.SysUserBackpackUpdateDto;
import com.qy.ntf.bean.entity.SysUserBackpack;
import com.qy.ntf.dao.SysUserBackpackDao;
import com.qy.ntf.service.SysUserBackpackService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 用户背包（记录虚拟商品购买订单标识） service服务实现
 */
@Service("sysUserBackpackService")
public class SysUserBackpackServiceImpl implements SysUserBackpackService {

  @Autowired private SysUserBackpackDao sysUserBackpackDao;

  @Override
  public BaseMapper<SysUserBackpack> getDao() {
    return sysUserBackpackDao;
  }

  @Override
  public IPage<SysUserBackpackDto> getListByPage(
      Class<SysUserBackpackDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysUserBackpack> queryWrapper) {
    IPage<SysUserBackpackDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysUserBackpackDto getSysUserBackpackById(Long id) {
    Optional<SysUserBackpackDto> optional = selectDataById(SysUserBackpackDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysUserBackpackAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysUserBackpackDto dto = new SysUserBackpackDto();
    md.map(tmpDto, dto);
    insertData(dto, SysUserBackpack.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysUserBackpackUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysUserBackpackDto dto = new SysUserBackpackDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysUserBackpackDto dto) {
    SysUserBackpackDto sysUserBackpack = getSysUserBackpackById(dto.getId());
    if (sysUserBackpack.getState() == 1) {
      sysUserBackpack.setState(2);
    } else {
      sysUserBackpack.setState(1);
    }
    sysUserBackpack.setUpdateTime(new Date());
    sysUserBackpack.setUpdateId(dto.getUpdateId());
    updateDataById(sysUserBackpack.getId(), sysUserBackpack);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysUserBackpackDto> list(SysUserBackpackDto dto) {
    LambdaQueryWrapper<SysUserBackpack> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysUserBackpackDto.class, queryWrapper);
  }
}
