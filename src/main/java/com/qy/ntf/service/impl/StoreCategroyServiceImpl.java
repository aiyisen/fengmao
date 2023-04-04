package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.StoreCategroyAddDto;
import com.qy.ntf.bean.dto.StoreCategroyDto;
import com.qy.ntf.bean.dto.StoreCategroyUpdateDto;
import com.qy.ntf.bean.entity.StoreCategroy;
import com.qy.ntf.dao.StoreCategroyDao;
import com.qy.ntf.service.StoreCategroyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-08-06 13:01:44 DESC : 藏品分类 service服务实现
 */
@Service("storeCategroyService")
public class StoreCategroyServiceImpl implements StoreCategroyService {

  @Autowired private StoreCategroyDao storeCategroyDao;

  @Override
  public BaseMapper<StoreCategroy> getDao() {
    return storeCategroyDao;
  }

  @Override
  public IPage<StoreCategroyDto> getListByPage(
      Class<StoreCategroyDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreCategroy> queryWrapper) {
    IPage<StoreCategroyDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public StoreCategroyDto getStoreCategroyById(Long id) {
    Optional<StoreCategroyDto> optional = selectDataById(StoreCategroyDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(StoreCategroyAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreCategroyDto dto = new StoreCategroyDto();
    md.map(tmpDto, dto);
    insertData(dto, StoreCategroy.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreCategroyUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreCategroyDto dto = new StoreCategroyDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(StoreCategroyDto dto) {
    StoreCategroyDto storeCategroy = getStoreCategroyById(dto.getId());
    if (storeCategroy.getState() == 1) {
      storeCategroy.setState(2);
    } else {
      storeCategroy.setState(1);
    }
    storeCategroy.setUpdateTime(new Date());
    storeCategroy.setUpdateId(dto.getUpdateId());
    updateDataById(storeCategroy.getId(), storeCategroy);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<StoreCategroyDto> list(StoreCategroyDto dto) {
    LambdaQueryWrapper<StoreCategroy> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(StoreCategroyDto.class, queryWrapper);
  }
}
