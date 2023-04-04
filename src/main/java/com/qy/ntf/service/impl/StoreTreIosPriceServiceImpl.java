package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.StoreTreIosPriceAddDto;
import com.qy.ntf.bean.dto.StoreTreIosPriceDto;
import com.qy.ntf.bean.dto.StoreTreIosPriceUpdateDto;
import com.qy.ntf.bean.entity.StoreTreIosPrice;
import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.dao.StoreTreIosPriceDao;
import com.qy.ntf.dao.StoreTreasureDao;
import com.qy.ntf.service.StoreTreIosPriceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.utils.Strings;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-07-31 20:17:27 DESC : ios价格 service服务实现
 */
@Service("storeTreIosPriceService")
public class StoreTreIosPriceServiceImpl implements StoreTreIosPriceService {

  @Autowired private StoreTreIosPriceDao storeTreIosPriceDao;

  @Override
  public BaseMapper<StoreTreIosPrice> getDao() {
    return storeTreIosPriceDao;
  }

  @Override
  public IPage<StoreTreIosPriceDto> getListByPage(
      Class<StoreTreIosPriceDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreTreIosPrice> queryWrapper) {
    IPage<StoreTreIosPriceDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public StoreTreIosPriceDto getStoreTreIosPriceById(Long id) {
    Optional<StoreTreIosPriceDto> optional = selectDataById(StoreTreIosPriceDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(StoreTreIosPriceAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreIosPriceDto dto = new StoreTreIosPriceDto();
    md.map(tmpDto, dto);
    insertData(dto, StoreTreIosPrice.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreTreIosPriceUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreIosPriceDto dto = new StoreTreIosPriceDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(StoreTreIosPriceDto dto) {
    StoreTreIosPriceDto storeTreIosPrice = getStoreTreIosPriceById(dto.getId());
    if (storeTreIosPrice.getState() == 1) {
      storeTreIosPrice.setState(2);
    } else {
      storeTreIosPrice.setState(1);
    }
    storeTreIosPrice.setUpdateTime(new Date());
    storeTreIosPrice.setUpdateId(dto.getUpdateId());
    updateDataById(storeTreIosPrice.getId(), storeTreIosPrice);
  }

  @Autowired private StoreTreasureDao storeTreasureDao;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    StoreTreIosPrice storeTreIosPrice = storeTreIosPriceDao.selectById(id);
    if (storeTreIosPrice != null) {
      LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery(StoreTreasure.class);
      que.eq(BaseEntity::getState, 1);
      que.eq(StoreTreasure::getStripId, id);
      List<StoreTreasure> storeTreasures = storeTreasureDao.selectList(que);
      if (storeTreasures.size() > 0) {
        throw new RuntimeException(
            "该价格已被："
                + Strings.join(
                    storeTreasures.stream()
                        .map(StoreTreasure::getTreasureTitle)
                        .collect(Collectors.toList()),
                    ",")
                + "藏品占用无法删除");
      }
      storeTreIosPrice.setState(-1);
      storeTreIosPriceDao.updateById(storeTreIosPrice);
    } else {
      throw new RuntimeException("id异常");
    }
  }

  @Override
  public List<StoreTreIosPriceDto> list(StoreTreIosPriceDto dto) {
    LambdaQueryWrapper<StoreTreIosPrice> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(BaseEntity::getState, 1);
    return selectList(StoreTreIosPriceDto.class, queryWrapper);
  }
}
