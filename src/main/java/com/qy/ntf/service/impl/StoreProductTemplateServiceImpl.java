package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.StoreProductTemplateAddDto;
import com.qy.ntf.bean.dto.StoreProductTemplateDto;
import com.qy.ntf.bean.dto.StoreProductTemplateUpdateDto;
import com.qy.ntf.bean.entity.StoreProductTemplate;
import com.qy.ntf.dao.StoreProductTemplateDao;
import com.qy.ntf.service.StoreProductTemplateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 特权商品材料模板 service服务实现
 */
@Service("storeProductTemplateService")
public class StoreProductTemplateServiceImpl implements StoreProductTemplateService {

  @Autowired private StoreProductTemplateDao storeProductTemplateDao;

  @Override
  public BaseMapper<StoreProductTemplate> getDao() {
    return storeProductTemplateDao;
  }

  @Override
  public IPage<StoreProductTemplateDto> getListByPage(
      Class<StoreProductTemplateDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<StoreProductTemplate> queryWrapper) {
    IPage<StoreProductTemplateDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public StoreProductTemplateDto getStoreProductTemplateById(Long id) {
    Optional<StoreProductTemplateDto> optional = selectDataById(StoreProductTemplateDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(StoreProductTemplateAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProductTemplateDto dto = new StoreProductTemplateDto();
    md.map(tmpDto, dto);
    insertData(dto, StoreProductTemplate.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(StoreProductTemplateUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreProductTemplateDto dto = new StoreProductTemplateDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(StoreProductTemplateDto dto) {
    StoreProductTemplateDto storeProductTemplate = getStoreProductTemplateById(dto.getId());
    if (storeProductTemplate.getState() == 1) {
      storeProductTemplate.setState(2);
    } else {
      storeProductTemplate.setState(1);
    }
    storeProductTemplate.setUpdateTime(new Date());
    storeProductTemplate.setUpdateId(dto.getUpdateId());
    updateDataById(storeProductTemplate.getId(), storeProductTemplate);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<StoreProductTemplateDto> list(StoreProductTemplateDto dto) {
    LambdaQueryWrapper<StoreProductTemplate> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(StoreProductTemplateDto.class, queryWrapper);
  }
}
