package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.SysMessageAddDto;
import com.qy.ntf.bean.dto.SysMessageDto;
import com.qy.ntf.bean.dto.SysMessageUpdateDto;
import com.qy.ntf.bean.dto.SysUserAdminDto;
import com.qy.ntf.bean.entity.SysMessage;
import com.qy.ntf.dao.SysMessageDao;
import com.qy.ntf.dao.SysUserDao;
import com.qy.ntf.service.SysMessageService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统信息 service服务实现
 */
@Service("sysMessageService")
public class SysMessageServiceImpl implements SysMessageService {

  @Autowired private SysMessageDao sysMessageDao;

  @Override
  public BaseMapper<SysMessage> getDao() {
    return sysMessageDao;
  }

  @Override
  public IPage<SysMessageDto> getListByPage(
      Class<SysMessageDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<SysMessage> queryWrapper) {
    IPage<SysMessageDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public SysMessageDto getSysMessageById(Long id) {
    Optional<SysMessageDto> optional = selectDataById(SysMessageDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Autowired private SysUserDao sysUserDao;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(SysMessageAddDto tmpDto, SysUserAdminDto adminUserData) {
    ModelMapper md = new ModelMapper();
    md.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    SysMessage sysMessage = new SysMessage();
    md.map(tmpDto, sysMessage);
    sysMessage.setMessage(tmpDto.getMessage());
    sysMessage.setMsgType(0);
    sysMessage.setCreateId(adminUserData.getId());
    sysMessage.setCreateTime(new Date());
    sysMessageDao.insert(sysMessage);
    //    List<SysUser> sysUsers = sysUserDao.selectList(Wrappers.lambdaQuery());
    //    AppJpushUtil.sendPush(
    //        sysUsers.stream().map(SysUser::getId).collect(Collectors.toList()),
    //        sysMessage.getMsgTitle(),
    //        sysMessage.getMessage());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(SysMessageUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysMessageDto dto = new SysMessageDto();
    md.map(tmpDto, dto);
    dto.setUpdateTime(new Date());
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(SysMessageDto dto) {
    SysMessageDto sysMessage = getSysMessageById(dto.getId());
    if (sysMessage.getState() == 1) {
      sysMessage.setState(2);
    } else {
      sysMessage.setState(1);
    }
    sysMessage.setUpdateTime(new Date());
    sysMessage.setUpdateId(dto.getUpdateId());
    updateDataById(sysMessage.getId(), sysMessage);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<SysMessageDto> list(SysMessageDto dto) {
    LambdaQueryWrapper<SysMessage> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(SysMessageDto.class, queryWrapper);
  }
}
