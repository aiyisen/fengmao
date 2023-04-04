package com.qy.ntf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.OrderVipAddDto;
import com.qy.ntf.bean.dto.OrderVipDto;
import com.qy.ntf.bean.dto.OrderVipUpdateDto;
import com.qy.ntf.bean.entity.ConBalanceRecord;
import com.qy.ntf.bean.entity.OrderTreasurePool;
import com.qy.ntf.bean.entity.OrderVip;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.LLNotifyParam;
import com.qy.ntf.dao.ConBalanceRecordDao;
import com.qy.ntf.dao.OrderVipDao;
import com.qy.ntf.dao.SysDictonaryDao;
import com.qy.ntf.dao.SysUserDao;
import com.qy.ntf.service.OrderVipService;
import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.wxPay.WXNotifyParam;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 王振读 2022-06-17 22:00:33 DESC : vip购买订单 service服务实现
 */
@Slf4j
@Service("orderVipService")
public class OrderVipServiceImpl implements OrderVipService {

  @Autowired private OrderVipDao orderVipDao;

  @Override
  public BaseMapper<OrderVip> getDao() {
    return orderVipDao;
  }

  @Override
  public IPage<OrderVipDto> getListByPage(
      Class<OrderVipDto> clazz,
      long currentPage,
      Long pageSize,
      LambdaQueryWrapper<OrderVip> queryWrapper) {
    IPage<OrderVipDto> selectPageList = selectPageList(clazz, currentPage, pageSize, queryWrapper);
    return selectPageList;
  }

  @Autowired private SysDictonaryDao sysDictonaryDao;

  @Override
  public void setFail(Long orderID) {
    OrderVip orderVip = orderVipDao.selectById(orderID);
    orderVip.setOrderFlag(-1);
    orderVipDao.updateById(orderVip);
    String vip_total_count = sysDictonaryDao.selectByAlias("vip_total_count");
    int integer = Integer.parseInt(vip_total_count);
    integer = integer + 1;
    sysDictonaryDao.updateByAlias("vip_total_count", integer);
  }

  @Override
  public void noifityWXin(WXNotifyParam wxp, boolean b) {
    log.info("noifityWXin-in:{}", wxp);
    try {
      log.debug("noifityWXin-flag:{}", b);
      if (wxp.getReturn_code().equals(ResourcesUtil.SUCCESS) && b) {
        String outtradeno = wxp.getOut_trade_no();
        String transactionId = wxp.getTransaction_id();

        log.info("微信异步通知参数：" + wxp);
        updateOrderByNotify(outtradeno, transactionId, 0);
      }
    } catch (Exception e) {
      log.error("noifityWXin-error:{}", e);
    }
  }

  @Autowired private SysUserDao sysUserDao;

  private void updateOrderByNotify(String outtradeno, String transactionId, int i) {
    OrderVip orderVip = orderVipDao.selectByTradeNo(outtradeno);
    if (orderVip != null && (orderVip.getOrderFlag() == 0 || orderVip.getOrderFlag() == -1)) {
      SysUser sysUser = sysUserDao.selectById(orderVip.getCreateId());
      //      OrderTreasurePool tmp = new OrderTreasurePool();
      //      tmp.setCreateId(sysUser.getId());
      //      tmp.setId(orderVip.getId());
      //      tmp.setTotalPrice(orderVip.getPayTotal());
      //      tmp.setPayType(i);
      //      addConBalanceRecord(tmp);
      String vip_days = sysDictonaryDao.selectByAlias("vip_days");
      int vipDay = Integer.parseInt(vip_days);
      if (sysUser.getVipEndTime() == null) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, vipDay);
        sysUser.setVipEndTime(instance.getTime());
        sysUserDao.updateById(sysUser);
      } else {
        if (sysUser.getVipEndTime().getTime() > System.currentTimeMillis()) {
          Calendar instance = Calendar.getInstance();
          instance.setTime(sysUser.getVipEndTime());
          instance.add(Calendar.DAY_OF_MONTH, vipDay);
          sysUser.setVipEndTime(instance.getTime());
          sysUserDao.updateById(sysUser);
        } else {
          Calendar instance = Calendar.getInstance();
          instance.add(Calendar.DAY_OF_MONTH, vipDay);
          sysUser.setVipEndTime(instance.getTime());
          sysUserDao.updateById(sysUser);
        }
      }
      orderVip.setOrderFlag(1);
      orderVip.setUpdateTime(new Date());
      orderVipDao.updateById(orderVip);
      ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
      conBalanceRecord.setUId(orderVip.getCreateId());
      conBalanceRecord.setReType(3);
      conBalanceRecord.setOrderId(orderVip.getId() + "");
      conBalanceRecord.setOrderType(5);
      conBalanceRecord.setTotalPrice(orderVip.getPayTotal());
      conBalanceRecord.setTradingChannel(i);
      conBalanceRecord.setCreateId(orderVip.getCreateId());
      conBalanceRecord.setCreateTime(new Date());
      conBalanceRecordDao.insert(conBalanceRecord);
    }
  }

  @Autowired private ConBalanceRecordDao conBalanceRecordDao;

  private void addConBalanceRecord(OrderTreasurePool orderProduct) {
    ConBalanceRecord conBalanceRecord = new ConBalanceRecord();
    conBalanceRecord.setUId(orderProduct.getCreateId());
    conBalanceRecord.setReType(2);
    conBalanceRecord.setOrderId(orderProduct.getId() + "");
    conBalanceRecord.setOrderType(5);
    conBalanceRecord.setTotalPrice(orderProduct.getTotalPrice());
    conBalanceRecord.setTradingChannel(orderProduct.getPayType());
    conBalanceRecord.setCreateId(orderProduct.getCreateId());
    conBalanceRecord.setCreateTime(new Date());
    conBalanceRecordDao.insert(conBalanceRecord);
  }

  @Override
  public void noifityAlipay(String outtradeno, String tradeNo) {
    updateOrderByNotify(outtradeno, tradeNo, 1);
  }

  @Override
  public void noifitylianlian(LLNotifyParam wxp) {
    log.info("noifitylianlian-in:{}", wxp);
    try {
      String outtradeno = wxp.getNo_order();
      String transactionId = wxp.getOid_paybill();

      log.info("连连pay orderVIp-异步通知参数：" + wxp);
      updateOrderByNotify(outtradeno, transactionId, 4);
    } catch (Exception e) {
      log.error("noifityWXin-error:{}", e);
    }
  }

  @Override
  public void lianlianRecharge(LLNotifyParam param) {}

  @Override
  public OrderVipDto getOrderVipById(Long id) {
    Optional<OrderVipDto> optional = selectDataById(OrderVipDto.class, id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return null;
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(OrderVipAddDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderVipDto dto = new OrderVipDto();
    md.map(tmpDto, dto);
    insertData(dto, OrderVip.class);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void update(OrderVipUpdateDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderVipDto dto = new OrderVipDto();
    md.map(tmpDto, dto);
    updateDataById(dto.getId(), dto);
  }

  @Override
  public void updateState(OrderVipDto dto) {
    OrderVipDto orderVip = getOrderVipById(dto.getId());
    if (orderVip.getState() == 1) {
      orderVip.setState(2);
    } else {
      orderVip.setState(1);
    }
    orderVip.setUpdateTime(new Date());
    orderVip.setUpdateId(dto.getUpdateId());
    updateDataById(orderVip.getId(), orderVip);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {
    deleteDataById(id);
  }

  @Override
  public List<OrderVipDto> list(OrderVipDto dto) {
    LambdaQueryWrapper<OrderVip> queryWrapper = new LambdaQueryWrapper<>();
    return selectList(OrderVipDto.class, queryWrapper);
  }
}
