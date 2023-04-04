package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysBannerAddDto;
import com.qy.ntf.bean.dto.SysBannerDto;
import com.qy.ntf.bean.dto.SysBannerUpdateDto;
import com.qy.ntf.bean.entity.SysBanner;
import com.qy.ntf.dao.SysBannerDao;
import com.qy.ntf.service.SysBannerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统轮播 service服务实现
 */
@Service("sysBannerService")
public class SysBannerServiceImpl implements SysBannerService {

  @Autowired private SysBannerDao sysBannerDao;

  @Override
  public BaseMapper<SysBanner> getDao() {
    return sysBannerDao;
  }

  @Override
  public IPage<SysBannerDto> getListByPage(
      Class<SysBannerDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysBanner> queryWrapper) {
    IPage<SysBannerDto> selectPageList = selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysBannerDto getSysBannerById(Long id) {
    Optional<SysBannerDto> optional = selectDataById(SysBannerDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysBannerAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysBannerDto dto = new SysBannerDto();
    md.map(tmpDto, dto);
    insertData(dto, SysBanner.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysBannerUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysBannerDto dto = new SysBannerDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysBannerDto dto) {
    SysBannerDto sysBanner = getSysBannerById(dto.getId());
    if (sysBanner.getState() == 1) {
      sysBanner.setState(2);
    } else {
      sysBanner.setState(1);
    }
    sysBanner.setUpdateTime(new Date());
    sysBanner.setUpdateId(dto.getUpdateId());
    updateDataById(sysBanner.getId(), sysBanner);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysBannerDto> list(Integer type) {
    LambdaQueryWrapper<SysBanner> que = new LambdaQueryWrapper<>();
    que.eq(BaseEntity::getState, 1)
        .eq(SysBanner::getBannerType, type)
        .orderByAsc(SysBanner::getOrderSum);
    return selectList(SysBannerDto.class, que);
  }
}
