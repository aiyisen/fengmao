package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.StoreProPoolCol;
import com.qy.ntf.dao.StoreProPoolColDao;
import com.qy.ntf.service.StoreProPoolColService;
import com.qy.ntf.util.PageSelectParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池藏品收藏 service服务实现
 */
@Service("storeProPoolColService")
public class StoreProPoolColServiceImpl implements StoreProPoolColService {

  @Autowired private StoreProPoolColDao storeProPoolColDao;

  @Override
  public BaseMapper<StoreProPoolCol> getDao() {
    return storeProPoolColDao;
  }

  @Override
  public IPage<StoreProPoolColDto> getListByPage(
      Class<StoreProPoolColDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreProPoolCol> queryWrapper) {
    IPage<StoreProPoolColDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public IPage<StoreProPoolDto> getListByPage(PageSelectParam<Object> param, UserDto userData) {

    return storeProPoolColDao.getListByPageAndUserId(
        new Page<>(param.getPageNum(), param.getPageSize()), userData.getId());
  }

  @Override
  public void deleteCollect(List<String> param, UserDto userData) {
    LambdaQueryWrapper<StoreProPoolCol> que = new LambdaQueryWrapper<>();
    que.eq(StoreProPoolCol::getUId, userData.getId()).in(StoreProPoolCol::getProPoolId, param);
    storeProPoolColDao.delete(que);
  }

  @Override
  public StoreProPoolColDto getStoreProPoolColById(Long id) {
    Optional<StoreProPoolColDto> optional = selectDataById(StoreProPoolColDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(StoreProPoolColAddDto tmpDto) {
    LambdaQueryWrapper<StoreProPoolCol> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper
        .eq(StoreProPoolCol::getProPoolId, tmpDto.getProPoolId())
        .eq(StoreProPoolCol::getUId, tmpDto.getUId());
    List<StoreProPoolColDto> storeProPoolColDtos =
        selectList(StoreProPoolColDto.class, queryWrapper);
    if (storeProPoolColDtos.size() > 0) throw new RuntimeException("该商品已收藏");
    ModelMapper md = new ModelMapper();
    StoreProPoolColDto dto = new StoreProPoolColDto();
    md.map(tmpDto, dto);
    insertData(dto, StoreProPoolCol.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreProPoolColUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProPoolColDto dto = new StoreProPoolColDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(StoreProPoolColDto dto) {
    StoreProPoolColDto storeProPoolCol = getStoreProPoolColById(dto.getId());
    if (storeProPoolCol.getState() == 1) {
      storeProPoolCol.setState(2);
    } else {
      storeProPoolCol.setState(1);
    }
    storeProPoolCol.setUpdateTime(new Date());
    storeProPoolCol.setUpdateId(dto.getUpdateId());
    updateDataById(storeProPoolCol.getId(), storeProPoolCol);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<StoreProPoolColDto> list(StoreProPoolColDto dto) {
    LambdaQueryWrapper<StoreProPoolCol> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(StoreProPoolColDto.class, queryWrapper);
  }
}
