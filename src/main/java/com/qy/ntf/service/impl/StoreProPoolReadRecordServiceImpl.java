package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.StoreProPoolReadRecordAddDto;
import com.qy.ntf.bean.dto.StoreProPoolReadRecordDto;
import com.qy.ntf.bean.dto.StoreProPoolReadRecordUpdateDto;
import com.qy.ntf.bean.entity.StoreProPoolReadRecord;
import com.qy.ntf.dao.StoreProPoolReadRecordDao;
import com.qy.ntf.service.StoreProPoolReadRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-28 00:02:44 DESC : service服务实现
 */
@Service("storeProPoolReadRecordService")
public class StoreProPoolReadRecordServiceImpl implements StoreProPoolReadRecordService {

  @Autowired private StoreProPoolReadRecordDao storeProPoolReadRecordDao;

  @Override
  public BaseMapper<StoreProPoolReadRecord> getDao() {
    return storeProPoolReadRecordDao;
  }

  @Override
  public IPage<StoreProPoolReadRecordDto> getListByPage(
      Class<StoreProPoolReadRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreProPoolReadRecord> queryWrapper) {
    IPage<StoreProPoolReadRecordDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public StoreProPoolReadRecordDto getStoreProPoolReadRecordById(Long id) {
    Optional<StoreProPoolReadRecordDto> optional =
        selectDataById(StoreProPoolReadRecordDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(StoreProPoolReadRecordAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProPoolReadRecordDto dto = new StoreProPoolReadRecordDto();
    md.map(tmpDto, dto);
    insertData(dto, StoreProPoolReadRecord.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreProPoolReadRecordUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProPoolReadRecordDto dto = new StoreProPoolReadRecordDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(StoreProPoolReadRecordDto dto) {
    StoreProPoolReadRecordDto storeProPoolReadRecord = getStoreProPoolReadRecordById(dto.getId());
    if (storeProPoolReadRecord.getState() == 1) {
      storeProPoolReadRecord.setState(2);
    } else {
      storeProPoolReadRecord.setState(1);
    }
    storeProPoolReadRecord.setUpdateTime(new Date());
    storeProPoolReadRecord.setUpdateId(dto.getUpdateId());
    updateDataById(storeProPoolReadRecord.getId(), storeProPoolReadRecord);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<StoreProPoolReadRecordDto> list(StoreProPoolReadRecordDto dto) {
    LambdaQueryWrapper<StoreProPoolReadRecord> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(StoreProPoolReadRecordDto.class, queryWrapper);
  }
}
