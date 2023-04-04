package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.customResult.InviteRecordRes;
import com.qy.ntf.bean.dto.ConIntegralRecordAddDto;
import com.qy.ntf.bean.dto.ConIntegralRecordDto;
import com.qy.ntf.bean.dto.ConIntegralRecordUpdateDto;
import com.qy.ntf.bean.dto.UserDto;
import com.qy.ntf.bean.entity.ConIntegralRecord;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.dao.ConIntegralRecordDao;
import com.qy.ntf.dao.SysDictonaryDao;
import com.qy.ntf.dao.SysUserDao;
import com.qy.ntf.service.ConIntegralRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 积分流水记录（记录总和>=0) service服务实现
 */
@Service("conIntegralRecordService")
public class ConIntegralRecordServiceImpl implements ConIntegralRecordService {

  @Autowired private ConIntegralRecordDao conIntegralRecordDao;

  @Override
  public BaseMapper<ConIntegralRecord> getDao() {
    return conIntegralRecordDao;
  }

  @Override
  public IPage<ConIntegralRecordDto> getListByPage(
      Class<ConIntegralRecordDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<ConIntegralRecord> queryWrapper) {
    IPage<ConIntegralRecordDto> selectPageList =
        selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Override
  public List<InviteRecordRes> getInviteRecord(UserDto userData) {
    return conIntegralRecordDao.getIntiteRecord(userData.getId());
  }

  @Override
  public Object todayGet(UserDto userData) {
    List<InviteRecordRes> result = conIntegralRecordDao.todayGet(userData.getId());
    if (result.size() > 0) {
      throw new RuntimeException("已领取");
    }
    return "SUCCESS";
  }

  @Autowired private SysUserDao sysUserDao;
  @Autowired private SysDictonaryDao sysDictonaryDao;

  @Override
  public Object getTodayIntegral(UserDto userData) {
    todayGet(userData);
    String in = sysDictonaryDao.selectByAlias("login_send_count");
    SysUser sysUser = sysUserDao.selectById(userData.getId());
    sysUser.setMetaCount(sysUser.getMetaCount() + Integer.parseInt(in));
    sysUserDao.updateById(sysUser);
    ConIntegralRecord re = new ConIntegralRecord();
    re.setUId(sysUser.getId());
    re.setRecordType(0);
    re.setMetaCount(Integer.valueOf(in));
    re.setCreateId(sysUser.getCreateId());
    re.setCreateTime(new Date());
    conIntegralRecordDao.insert(re);
    return "SUCCESS";
  }

  @Override
  public ConIntegralRecordDto getConIntegralRecordById(Long id) {
    Optional<ConIntegralRecordDto> optional = selectDataById(ConIntegralRecordDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(ConIntegralRecordAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    ConIntegralRecordDto dto = new ConIntegralRecordDto();
    md.map(tmpDto, dto);
    insertData(dto, ConIntegralRecord.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(ConIntegralRecordUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    ConIntegralRecordDto dto = new ConIntegralRecordDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(ConIntegralRecordDto dto) {
    ConIntegralRecordDto conIntegralRecord = getConIntegralRecordById(dto.getId());
    if (conIntegralRecord.getState() == 1) {
      conIntegralRecord.setState(2);
    } else {
      conIntegralRecord.setState(1);
    }
    conIntegralRecord.setUpdateTime(new Date());
    conIntegralRecord.setUpdateId(dto.getUpdateId());
    updateDataById(conIntegralRecord.getId(), conIntegralRecord);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<ConIntegralRecordDto> list(ConIntegralRecordDto dto) {
    LambdaQueryWrapper<ConIntegralRecord> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(ConIntegralRecordDto.class, queryWrapper);
  }
}
