package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysIdeaAddDto;
import com.qy.ntf.bean.dto.SysIdeaDto;
import com.qy.ntf.bean.dto.SysIdeaUpdateDto;
import com.qy.ntf.bean.entity.SysIdea;
import com.qy.ntf.dao.SysIdeaDao;
import com.qy.ntf.service.SysIdeaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 反馈建议 service服务实现
 */
@Service("sysIdeaService")
public class SysIdeaServiceImpl implements SysIdeaService {

  @Autowired private SysIdeaDao sysIdeaDao;

  @Override
  public BaseMapper<SysIdea> getDao() {
    return sysIdeaDao;
  }

  @Override
  public IPage<SysIdeaDto> getListByPage(
      Class<SysIdeaDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysIdea> queryWrapper) {
    IPage<SysIdeaDto> selectPageList = selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysIdeaDto getSysIdeaById(Long id) {
    Optional<SysIdeaDto> optional = selectDataById(SysIdeaDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysIdeaAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysIdeaDto dto = new SysIdeaDto();
    md.map(tmpDto, dto);
    insertData(dto, SysIdea.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysIdeaUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysIdeaDto dto = new SysIdeaDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysIdeaDto dto) {
    SysIdeaDto sysIdea = getSysIdeaById(dto.getId());
    if (sysIdea.getState() == 1) {
      sysIdea.setState(2);
    } else {
      sysIdea.setState(1);
    }
    sysIdea.setUpdateTime(new Date());
    sysIdea.setUpdateId(dto.getUpdateId());
    updateDataById(sysIdea.getId(), sysIdea);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysIdeaDto> list(SysIdeaDto dto) {
    LambdaQueryWrapper<SysIdea> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysIdeaDto.class, queryWrapper);
  }
}
