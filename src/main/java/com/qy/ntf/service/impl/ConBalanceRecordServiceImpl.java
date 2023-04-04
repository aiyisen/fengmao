package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.ConBalanceRecordAddDto;
import com.qy.ntf.bean.dto.ConBalanceRecordDto;
import com.qy.ntf.bean.dto.ConBalanceRecordUpdateDto;
import com.qy.ntf.bean.entity.ConBalanceRecord;
import com.qy.ntf.dao.ConBalanceRecordDao;
import com.qy.ntf.service.ConBalanceRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 余额记录 service服务实现
 */
@Service("conBalanceRecordService")
public class ConBalanceRecordServiceImpl implements ConBalanceRecordService {

  @Autowired private ConBalanceRecordDao conBalanceRecordDao;

  @Override
  public BaseMapper<ConBalanceRecord> getDao() {
    return conBalanceRecordDao;
  }

  @Override
  public IPage<ConBalanceRecordDto> getListByPage(
      Class<ConBalanceRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<ConBalanceRecord> queryWrapper) {
    IPage<ConBalanceRecordDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public ConBalanceRecordDto getConBalanceRecordById(Long id) {
    Optional<ConBalanceRecordDto> optional = selectDataById(ConBalanceRecordDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(ConBalanceRecordAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    ConBalanceRecordDto dto = new ConBalanceRecordDto();
    md.map(tmpDto, dto);
    insertData(dto, ConBalanceRecord.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(ConBalanceRecordUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    ConBalanceRecordDto dto = new ConBalanceRecordDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(ConBalanceRecordDto dto) {
    ConBalanceRecordDto conBalanceRecord = getConBalanceRecordById(dto.getId());
    if (conBalanceRecord.getState() == 1) {
      conBalanceRecord.setState(2);
    } else {
      conBalanceRecord.setState(1);
    }
    conBalanceRecord.setUpdateTime(new Date());
    conBalanceRecord.setUpdateId(dto.getUpdateId());
    updateDataById(conBalanceRecord.getId(), conBalanceRecord);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<ConBalanceRecordDto> list(ConBalanceRecordDto dto) {
    LambdaQueryWrapper<ConBalanceRecord> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(ConBalanceRecordDto.class, queryWrapper);
  }
}
