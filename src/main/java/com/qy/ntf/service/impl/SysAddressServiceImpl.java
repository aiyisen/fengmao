package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysAddressAddDto;
import com.qy.ntf.bean.dto.SysAddressDto;
import com.qy.ntf.bean.dto.SysAddressUpdateDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.SysAddress;
import com.qy.ntf.dao.SysAddressDao;
import com.qy.ntf.service.SysAddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 收货地址 service服务实现
 */
@Service("sysAddressService")
public class SysAddressServiceImpl implements SysAddressService {

  @Autowired private SysAddressDao sysAddressDao;

  @Override
  public BaseMapper<SysAddress> getDao() {
    return sysAddressDao;
  }

  @Override
  public IPage<SysAddressDto> getListByPage(
      Class<SysAddressDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysAddress> queryWrapper) {
    IPage<SysAddressDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysAddress updateDefault(Long id, UserDto userData) {
    SysAddress sysAddress = sysAddressDao.selectById(id);
    if (!Objects.equals(sysAddress.getUId(), userData.getId())) {
      throw new RuntimeException("收货地址信息与令牌不一致");
    }
    LambdaQueryWrapper<SysAddress> que = new LambdaQueryWrapper<>();
    que.eq(BaseEntity::getCreateId, userData.getId());
    List<SysAddress> sysAddresses = sysAddressDao.selectList(que);
    sysAddresses.forEach(
        o -> {
          o.setIsDefault(0);
          sysAddressDao.updateById(o);
        });
    sysAddress.setIsDefault(1);
    sysAddressDao.updateById(sysAddress);
    return sysAddress;
  }

  @Override
  public SysAddressDto getSysAddressById(Long id) {
    Optional<SysAddressDto> optional = selectDataById(SysAddressDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysAddressAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysAddressDto dto = new SysAddressDto();
    md.map(tmpDto, dto);

    insertData(dto, SysAddress.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysAddressUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysAddressDto dto = new SysAddressDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysAddressDto dto) {
    SysAddressDto sysAddress = getSysAddressById(dto.getId());
    if (sysAddress.getState() == 1) {
      sysAddress.setState(2);
    } else {
      sysAddress.setState(1);
    }
    sysAddress.setUpdateTime(new Date());
    sysAddress.setUpdateId(dto.getUpdateId());
    updateDataById(sysAddress.getId(), sysAddress);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    SysAddress sysAddress = sysAddressDao.selectById(id);
    if (sysAddress != null) {
      sysAddress.setState(-1);
      sysAddressDao.updateById(sysAddress);
    }
  }

  @Override
  public List<SysAddressDto> list(SysAddressDto dto) {
    LambdaQueryWrapper<SysAddress> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysAddressDto.class, queryWrapper);
  }
}
