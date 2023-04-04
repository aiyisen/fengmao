package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysDictonaryAddDto;
import com.qy.ntf.bean.dto.SysDictonaryDto;
import com.qy.ntf.bean.dto.SysDictonaryUpdateDto;
import com.qy.ntf.bean.entity.SysDictonary;
import com.qy.ntf.dao.SysDictonaryDao;
import com.qy.ntf.service.SysDictonaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-27 21:29:38 DESC : service服务实现
 */
@Service("sysDictonaryService")
public class SysDictonaryServiceImpl implements SysDictonaryService {

  @Autowired private SysDictonaryDao sysDictonaryDao;

  @Override
  public BaseMapper<SysDictonary> getDao() {
    return sysDictonaryDao;
  }

  @Override
  public IPage<SysDictonaryDto> getListByPage(
      Class<SysDictonaryDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysDictonary> queryWrapper) {
    IPage<SysDictonaryDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public String getDicByAlias(String alias) {
    return sysDictonaryDao.selectByAlias(alias);
  }

  @Override
  public List<String> valueList(String alias) {
    return sysDictonaryDao.valueList(alias);
  }

  @Override
  public SysDictonaryDto getSysDictonaryById(Long id) {
    Optional<SysDictonaryDto> optional = selectDataById(SysDictonaryDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysDictonaryAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysDictonaryDto dto = new SysDictonaryDto();
    md.map(tmpDto, dto);
    insertData(dto, SysDictonary.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysDictonaryUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysDictonaryDto dto = new SysDictonaryDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysDictonaryDto dto) {
    SysDictonaryDto sysDictonary = getSysDictonaryById(dto.getId());
    if (sysDictonary.getState() == 1) {
      sysDictonary.setState(2);
    } else {
      sysDictonary.setState(1);
    }
    sysDictonary.setUpdateTime(new Date());
    sysDictonary.setUpdateId(dto.getUpdateId());
    updateDataById(sysDictonary.getId(), sysDictonary);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    SysDictonary sysDictonary = sysDictonaryDao.selectById(id);
    if (sysDictonary == null) throw new RuntimeException("id异常");
    deleteDataById(id);
  }

  @Override
  public List<SysDictonaryDto> list(SysDictonaryDto dto) {
    LambdaQueryWrapper<SysDictonary> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysDictonaryDto.class, queryWrapper);
  }
}
