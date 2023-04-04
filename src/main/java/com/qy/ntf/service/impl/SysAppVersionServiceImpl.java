package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.ntf.bean.dto.SysAppVersionDto;
import com.qy.ntf.bean.dto.SysAppVersionUpdateDto;
import com.qy.ntf.bean.entity.SysAppVersion;
import com.qy.ntf.dao.SysAppVersionDao;
import com.qy.ntf.service.SysAppVersionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 王振读 2022-07-22 00:02:04 DESC : service服务实现
 */
@Service("sysAppVersionService")
public class SysAppVersionServiceImpl implements SysAppVersionService {

  @Autowired private SysAppVersionDao sysAppVersionDao;

  @Override
  public BaseMapper<SysAppVersion> getDao() {
    return sysAppVersionDao;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysAppVersionUpdateDto tmpDto) {
    SysAppVersion sysAppVersion = sysAppVersionDao.selectById(tmpDto.getId());
    if (sysAppVersion != null) {
      ModelMapper md = new ModelMapper();
      SysAppVersionDto dto = new SysAppVersionDto();

      md.map(tmpDto, dto);
      dto.setCtype(sysAppVersion.getCtype());
      updateDataById(dto.getId(), dto);
    } else {
      throw new RuntimeException("id异常");
    }
  }

  @Override
  public List<SysAppVersionDto> list(SysAppVersionDto dto) {
    LambdaQueryWrapper<SysAppVersion> queryWrapper = new LambdaQueryWrapper<>();
    if (dto.getCtype() != null) {
      queryWrapper.eq(SysAppVersion::getCtype, dto.getCtype());
    }
    return selectList(SysAppVersionDto.class, queryWrapper);
  }
}
