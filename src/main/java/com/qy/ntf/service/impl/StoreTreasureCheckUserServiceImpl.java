package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserAddDto;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserDto;
import com.qy.ntf.bean.dto.StoreTreasureCheckUserUpdateDto;
import com.qy.ntf.bean.entity.StoreTreasureCheckUser;
import com.qy.ntf.dao.StoreTreasureCheckUserDao;
import com.qy.ntf.service.StoreTreasureCheckUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 超亲申购中奖名单 service服务实现
 */
@Service("storeTreasureCheckUserService")
public class StoreTreasureCheckUserServiceImpl implements StoreTreasureCheckUserService {

  @Autowired private StoreTreasureCheckUserDao storeTreasureCheckUserDao;

  @Override
  public BaseMapper<StoreTreasureCheckUser> getDao() {
    return storeTreasureCheckUserDao;
  }

  @Override
  public IPage<StoreTreasureCheckUserDto> getListByPage(
      Class<StoreTreasureCheckUserDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreTreasureCheckUser> queryWrapper) {
    IPage<StoreTreasureCheckUserDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public StoreTreasureCheckUserDto getStoreTreasureCheckUserById(Long id) {
    Optional<StoreTreasureCheckUserDto> optional =
        selectDataById(StoreTreasureCheckUserDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(StoreTreasureCheckUserAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreasureCheckUserDto dto = new StoreTreasureCheckUserDto();
    md.map(tmpDto, dto);
    insertData(dto, StoreTreasureCheckUser.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreTreasureCheckUserUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreasureCheckUserDto dto = new StoreTreasureCheckUserDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(StoreTreasureCheckUserDto dto) {
    StoreTreasureCheckUserDto storeTreasureCheckUser = getStoreTreasureCheckUserById(dto.getId());
    if (storeTreasureCheckUser.getState() == 1) {
      storeTreasureCheckUser.setState(2);
    } else {
      storeTreasureCheckUser.setState(1);
    }
    storeTreasureCheckUser.setUpdateTime(new Date());
    storeTreasureCheckUser.setUpdateId(dto.getUpdateId());
    updateDataById(storeTreasureCheckUser.getId(), storeTreasureCheckUser);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<StoreTreasureCheckUserDto> list(StoreTreasureCheckUserDto dto) {
    LambdaQueryWrapper<StoreTreasureCheckUser> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(StoreTreasureCheckUserDto.class, queryWrapper);
  }
}
