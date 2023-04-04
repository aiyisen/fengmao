package com.qy.ntf.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.customResult.CellResult;
import com.qy.ntf.bean.customResult.CellTotal;
import com.qy.ntf.bean.customResult.MobileByTokenRes;
import com.qy.ntf.bean.customResult.MyTreasure;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.*;
import com.qy.ntf.bean.param.*;
import com.qy.ntf.config.JWTToken;
import com.qy.ntf.config.UMSign;
import com.qy.ntf.dao.*;
import com.qy.ntf.util.*;
import com.qy.ntf.util.wenchang.DDCSdkClient;
import com.qy.ntf.util.wenchang.util.UserSignEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.modelmapper.ModelMapper;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.service @ClassName: UserService @Author:
 * 王振读 @Description: ${description} @Date: 2021/11/29 9:21 @Version: 1.0
 */
@Service
@Slf4j
public class SysUserService implements BaseService<UserDto, SysUser> {
  @Autowired private SysUserMapper userDao;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private MenuService menuService;
  @Autowired private OrganizationDao organizationDao;
  @Autowired private JwtTokenUtil jwtTokenUtil;

  public List<SysUser> findUserByTop(TopSelectParam param) {

    return sysUserDao.findUserByTop(param);
  }

  @Value("${password-key}")
  private String passwordKey;

  @Override
  public SysUserMapper getDao() {
    return this.userDao;
  }

  public List<SysUser> selectList() {
    LambdaQueryWrapper<SysUser> qe = new LambdaQueryWrapper<>();
    return userDao.selectList(qe);
  }

  public UserInfo checkLogin(LoginParam param) {
    List<SysUser> sysUsers =
        userDao.selectListByNameAndPassword(
            param.getAdminAccount(),
            EncryptUtil.getInstance().MD5(param.getPassword(), passwordKey));
    if (sysUsers.size() == 1 && sysUsers.get(0).getState() == 1) {
      sysUsers.get(0).setPass(null);
      String token = jwtTokenUtil.generateToken(sysUsers.get(0), null);
      redisTemplate
          .opsForValue()
          .set(token, JSONObject.toJSONString(sysUsers.get(0)), 30, TimeUnit.DAYS);
      Subject subject = SecurityUtils.getSubject();
      subject.login(new JWTToken(token));

      UserInfo result = new UserInfo();
      result.setUser(sysUsers.get(0));
      result.setToken(token);
      List<Map<String, Object>> dynamicRoutingList =
          menuService.getDynamicRoutingList(sysUsers.get(0).getId());
      result.setMenusList(dynamicRoutingList);
      return result;
    }

    return null;
  }

  public SysUser selectDataById(Long userId) {
    SysUser sysUser = userDao.selectById(userId);
    return sysUser;
  }

  // 取当前用户的整个组织机构, 不管是不是管理员
  public List<OrganizationDto> organizationTree(String root) {
    LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Organization::getCode, root);
    List<Organization> list = organizationDao.selectList(queryWrapper);
    ModelMapper modelMapper = new ModelMapper();
    List<OrganizationDto> dtoList =
        list.stream()
            .map(e -> modelMapper.map(e, OrganizationDto.class))
            .collect(Collectors.toList());

    dtoList.forEach(
        organization -> {
          List<OrganizationDto> children = childrenOrganization(organization.getCode());
          if (children.size() > 0) {
            organization.setChildren(children);
          }
        });
    return dtoList;
  }

  private List<OrganizationDto> childrenOrganization(String code) {

    LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByAsc(Organization::getCode);
    // 二级及三级，四级。。。 查询条件： like 'xxxx%' and length(code) = (参数代码长度 + 4)
    queryWrapper.likeRight(Organization::getCode, code);
    queryWrapper.apply("length(code)=" + (code.length() + 4));
    ModelMapper modelMapper = new ModelMapper();
    List<OrganizationDto> list =
        organizationDao.selectList(queryWrapper).stream()
            .map(e -> modelMapper.map(e, OrganizationDto.class))
            .collect(Collectors.toList());

    list.forEach(
        organization -> {
          List<OrganizationDto> children = childrenOrganization(organization.getCode());
          if (children.size() > 0) {
            organization.setChildren(children);
          }
        });
    return list;
  }

  public void updateUserState(Long id, Integer state) {
    userDao.updateUserState(id, state);
  }

  public ApiResponse<Object> checkUserName(SysUserAdmin user) {
    ApiResponse<Object> success = ApiResponse.success();
    success.setCode(0);
    success.setMsg("");
    LambdaQueryWrapper<SysUserAdmin> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SysUserAdmin::getAdminName, user.getAdminName());
    if (user.getId() != null) {
      queryWrapper.ne(SysUserAdmin::getId, user.getId());
    }
    List<SysUserAdmin> selectList = sysUserAdminDao.selectList(queryWrapper);
    if (selectList.size() > 0) {
      success.setCode(1);
      success.setMsg("用户名重复");
    }
    return success;
  }

  public void deleteUserById(Long id) throws Exception {
    Integer count = userDao.getRoleCount(id);
    if (count > 0) throw new Exception("该用户下有角色数据，不能删除");
    this.deleteDataById(id);
  }

  public void resetPassword(Long id, String password) {
    userDao.resetPassword(id, password);
  }

  @Autowired private SysDictonaryDao sysDictonaryDao;
  @Autowired private ConIntegralRecordDao conIntegralRecordDao;
  @Autowired private SysUserInviteLinkDao sysUserInviteLinkDao;
  @Autowired private RedissonClient redissonClient;

  @Transactional(rollbackFor = Exception.class)
  public UserInfo loginWithCode(LoginWithCodeParam param) {
    LambdaQueryWrapper<SysUser> que = new LambdaQueryWrapper<>();
    que.eq(SysUser::getPhone, param.getPhone());
    que.ne(BaseEntity::getState, -1);
    List<SysUser> sysUsers = userDao.selectList(que);

    if (sysUsers.size() > 0) {
      // 有
      if (sysUsers.get(0).getState() == 0) throw new RuntimeException("用户已被冻结");
      sysUsers.get(0).setPass(null);
      String token = jwtTokenUtil.generateToken(sysUsers.get(0), null);
      redisTemplate
          .opsForValue()
          .set(token, JSONObject.toJSONString(sysUsers.get(0)), 30, TimeUnit.DAYS);
      Object oldToken = redisTemplate.opsForHash().get("allToken", param.getPhone());
      if (oldToken != null) {
        Boolean delete = redisTemplate.delete(oldToken.toString());
        redisTemplate.opsForHash().put("allToken", param.getPhone(), token);
      } else {
        redisTemplate.opsForHash().put("allToken", param.getPhone(), token);
      }
      Subject subject = SecurityUtils.getSubject();
      subject.login(new JWTToken(token));

      UserInfo result = new UserInfo();
      if (sysUsers.get(0).getVipEndTime() != null) {
        if (sysUsers.get(0).getVipEndTime().getTime() > System.currentTimeMillis()) {
          sysUsers.get(0).setIsVip(1);
        } else {
          sysUsers.get(0).setIsVip(0);
        }
      }
      result.setUser(sysUsers.get(0));
      result.setToken(token);
      result.setTreaOrderCount(orderTreasurePoolDao.getCountByUserId(sysUsers.get(0).getId()));
      LambdaQueryWrapper<SysBeforeUser> q = Wrappers.lambdaQuery(SysBeforeUser.class);
      q.eq(SysBeforeUser::getPhone, sysUsers.get(0).getPhone());
      List<SysBeforeUser> sysBeforeUsers = sysBeforeUserDao.selectList(q);
      if (sysBeforeUsers.size() > 0) {
        result.setIsBeforeUser(1);
      }
      return result;
    } else {
      SysUser inviteUser = null;
      //      if (param.getInviteCode() != null && !"".equals(param.getInviteCode())) {
      //        LambdaQueryWrapper<SysUser> q = new LambdaQueryWrapper<>();
      //        q.eq(SysUser::getUserIndex, param.getInviteCode());
      //        List<SysUser> inviteUsers = userDao.selectList(q);
      //        if (inviteUsers.size() != 0) {
      //          // 添加邀请积分
      //          inviteUser = inviteUsers.get(0);
      //        } else {
      //          throw new RuntimeException("邀请码不正确，请核对");
      //        }
      //      }
      // 注册
      String uuid = null;
      que.clear();
      que.eq(SysUser::getUserIndex, uuid);
      for (int j = 0; j < 100; j++) {
        List<String> uuids = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
          uuids.add(IdUtil.getRamdomIndex());
        }
        que.clear();
        List<SysUser> allUsers = userDao.selectList(new LambdaQueryWrapper<>());
        uuids =
            uuids.stream()
                .filter(
                    o ->
                        !allUsers.stream()
                            .map(SysUser::getUserIndex)
                            .collect(Collectors.toList())
                            .contains(o))
                .collect(Collectors.toList());
        if (uuids.size() > 0) {
          j = 102;
          uuid = uuids.get(0);
        } else {
          log.info("查询用户邀请码失败：" + Strings.join(uuids, ','));
          if (j == 99) {
            j = 0;
          }
        }
      }
      SysUser sysUser = new SysUser();
      sysUser.setUserIndex(uuid);
      sysUser.setPhone(param.getPhone());
      sysUser.setCreateTime(new Date());
      //      WenchangDDC bsnUtil = new WenchangDDC();
      //      com.qy.ntf.util.wenchang.dto.Account count = bsnUtil.createCount();
      //      String resCode = bsnUtil.create(sysUser.getPhone(), count);
      //      bsnUtil.checkAccountCreateStatus(resCode);
      //      sysUser.setLinkAddress("0x" + bsnUtil.accountBech32ToHex(count.getAddress()));
      sysUser.setLinkAddress(AvataUtil.createUser(param.getPhone(), UUID.randomUUID().toString()));
      //      sysUser.setPublicKey(count.getPublicKey());
      //      sysUser.setPrivateKey(count.getPrivateKey());
      //      sysUser.setMnemonic(count.getKeyseed());
      sysUser.setPass(param.getPass());

      //      }
      sysUser.setUsername("新用户");
      userDao.insert(sysUser);
      sysUser.setCreateId(sysUser.getId());
      // 新建用户能量充值
      //      bsnUtil.userAddGas(sysUser.getLinkAddress());
      // 授权平台账户
      //      RBlockingQueue<Object> blockingFairQueue =
      //          redissonClient.getBlockingQueue("delay_queue_call");
      //      RDelayedQueue<Object> delayedQueue =
      // redissonClient.getDelayedQueue(blockingFairQueue);
      //      delayedQueue.offer("add_gas_" + sysUser.getId(), 1, TimeUnit.MINUTES);
      //      delayedQueue.offer("user_approve_" + sysUser.getId(), 3, TimeUnit.MINUTES);
      userDao.updateById(sysUser);
      String token = jwtTokenUtil.generateToken(sysUser, null);
      redisTemplate.opsForValue().set(token, JSONObject.toJSONString(sysUser), 30, TimeUnit.DAYS);
      Object oldToken = redisTemplate.opsForHash().get("allToken", param.getPhone());
      if (oldToken != null) {
        Boolean delete = redisTemplate.delete(oldToken.toString());
        redisTemplate.opsForHash().put("allToken", param.getPhone(), token);
      } else {
        redisTemplate.opsForHash().put("allToken", param.getPhone(), token);
      }
      Subject subject = SecurityUtils.getSubject();
      subject.login(new JWTToken(token));

      UserInfo result = new UserInfo();
      sysUser.setPass(null);
      result.setUser(sysUser);
      result.setToken(token);
      // 分销积分给上游用户
      if (inviteUser != null) {
        String integer = sysDictonaryDao.selectByAlias("invite_user_integer");
        inviteUser.setMetaCount(
            new BigDecimal(inviteUser.getMetaCount()).add(new BigDecimal(integer)).longValue());
        userDao.updateById(inviteUser);
        ConIntegralRecord conIntegralRecord = new ConIntegralRecord();
        conIntegralRecord.setUId(inviteUser.getId());
        conIntegralRecord.setRecordType(1);
        conIntegralRecord.setMetaCount(Integer.valueOf(integer));
        conIntegralRecord.setOrderId(result.getUser().getId());
        conIntegralRecord.setCreateId(inviteUser.getId());
        conIntegralRecord.setCreateTime(new Date());
        conIntegralRecordDao.insert(conIntegralRecord);
        SysUserInviteLink sysUserInviteLink = new SysUserInviteLink();
        sysUserInviteLink.setUId(inviteUser.getId());
        sysUserInviteLink.setSeUId(result.getUser().getId());
        sysUserInviteLink.setIntegralCount(Long.valueOf(integer));
        sysUserInviteLinkDao.insert(sysUserInviteLink);
      }
      return result;
    }
  }

  public SysUser getUserByPhone(String phone) {
    LambdaQueryWrapper<SysUser> que = new LambdaQueryWrapper<>();
    que.eq(SysUser::getPhone, phone);
    que.ne(BaseEntity::getState, -1);
    List<SysUser> sysUsers = userDao.selectList(que);
    if (sysUsers.size() > 0) {
      SysUser sysUser = sysUsers.get(0);
      sysUser.setTreaOrderCount(orderTreasurePoolDao.getCountByUserId(sysUsers.get(0).getId()));
      LambdaQueryWrapper<SysBeforeUser> q = Wrappers.lambdaQuery(SysBeforeUser.class);
      q.eq(SysBeforeUser::getPhone, sysUser.getPhone());
      List<SysBeforeUser> sysBeforeUsers = sysBeforeUserDao.selectList(q);
      if (sysBeforeUsers.size() > 0) {
        sysUser.setIsBeforeUser(1);
      }
      return sysUser;
    } else {
      return null;
    }
  }

  public UserInfo updatePhone(String token, String newPhone) {
    String json = redisTemplate.opsForValue().get(token);
    if (json != null) {
      SysUser newPhoneUser = sysUserDao.selectByMobile(newPhone);
      if (newPhoneUser != null) throw new RuntimeException("该手机号已注册请更换其他手机号");
      UserDto userDto = JSONObject.parseObject(json, UserDto.class);
      SysUser sysUser = userDao.selectById(userDto.getId());
      sysUser.setPhone(newPhone);
      sysUser.setUpdateTime(new Date());
      userDao.updateById(sysUser);
      redisTemplate.opsForValue().set(token, JSONObject.toJSONString(sysUser), 30, TimeUnit.DAYS);
      UserInfo result = new UserInfo();
      result.setUser(sysUser);
      result.setToken(token);
      return result;
    }
    throw new RuntimeException("获取用户信息异常");
  }

  public List<SysUser> getUserByIndex(String userIndex) {
    LambdaQueryWrapper<SysUser> que = new LambdaQueryWrapper<>();
    que.like(SysUser::getUserIndex, userIndex);
    List<SysUser> sysUsers = userDao.selectList(que);
    sysUsers.forEach(o -> o.setPass(null));
    return sysUsers;
  }

  @Autowired private SysUserDao sysUserDao;
  @Autowired private OrderTreasurePoolDao orderTreasurePoolDao;
  @Autowired private SysUserBackpackDao sysUserBackpackDao;
  @Autowired private StoreTreasureRecordDao storeTreasureRecordDao;
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;

  @Transactional
  public Object givingTreasure(GivingParam param, UserDto userData) {
    if (param.getType() == 1) {
      // 赠送藏品
      if (Strings.isEmpty(param.getBeGivingUserId())) throw new RuntimeException("用户不存在");
      SysUser sysUser = sysUserDao.selectByInviteCode(param.getBeGivingUserId());
      if (sysUser == null) throw new RuntimeException("用户不存在");
      List<SysUserBackpack> sysUserBackpacks =
          sysUserBackpackDao.selectByOrderFigerprintAndUserId(
              param.getOrderFingerprint(), userData.getId());
      SysUserBackpack first = sysUserBackpacks.get(0);
      if (first.getFinType() == 1 || first.getFinType() == 3 || first.getFinType() == 7) {
        throw new RuntimeException("该藏品已不存在您的账户下");
      }
      LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery();
      que.eq(StoreTreasure::getOrderFingerprint, param.getOrderFingerprint())
          .eq(BaseEntity::getCreateId, userData.getId())
          .orderByDesc(BaseEntity::getCreateTime);
      List<StoreTreasure> storeProPools = storeTreasureDao.selectList(que);
      if (storeProPools.size() > 0 && storeProPools.get(0).getState() == 1) {
        throw new RuntimeException("已挂牌出售无法赠与");
      } else if (storeProPools.size() > 0) {
        StoreTreasure storeTreasure = storeProPools.get(0);
        storeTreasure.setIsSale(2);
        //        storeTreasure.setOrderFingerprint(param.getOrderFingerprint());
        storeTreasure.setUpdateTime(new Date());
        storeTreasureDao.updateById(storeTreasure);
      }
      OrderTreasurePool orderTreasurePool =
          orderTreasurePoolDao.selectByOrderFingerpint(
              param.getOrderFingerprint()); // 查询该指纹最后一条成交订单，来确认其来源
      // 获赠记录
      SysUserBackpack sysUserBackpack = new SysUserBackpack();
      WenchangDDC bsnUtils = new WenchangDDC();
      SysUser send = sysUserDao.selectById(userData.getId());
      orderTreasurePoolService.transDDc(
          send.getLinkAddress(), sysUser, storeProPools.get(0), sysUserBackpack, bsnUtils);

      sysUserBackpack.setUId(sysUser.getId());
      if (orderTreasurePool != null) {
        sysUserBackpack.setSTreasureId(orderTreasurePool.getTeaPoId());
        sysUserBackpack.setOrderTreasurePoolId(orderTreasurePool.getId());
        sysUserBackpack.setTreasureFrom(orderTreasurePool.getItemType());
        sysUserBackpack.setIsCheck(orderTreasurePool.getItemType() == 3 ? 0 : null);
      } else {
        sysUserBackpack.setSTreasureId(first.getSTreasureId());
        sysUserBackpack.setOrderTreasurePoolId(-1L);
        sysUserBackpack.setTreasureFrom(0);
        sysUserBackpack.setIsCheck(null);
      }
      sysUserBackpack.setOrderFingerprint(param.getOrderFingerprint());
      sysUserBackpack.setBeforeUserId(userData.getId());
      sysUserBackpack.setAfterUserId(sysUser.getId());
      sysUserBackpack.setFinType(2);
      sysUserBackpack.setCreateId(sysUser.getId());
      sysUserBackpack.setCreateTime(new Date());
      sysUserBackpackDao.insert(sysUserBackpack);
      // 赠出记录
      SysUserBackpack out = new SysUserBackpack();
      out.setUId(userData.getId());
      if (orderTreasurePool != null) {
        out.setSTreasureId(orderTreasurePool.getTeaPoId());
        out.setOrderTreasurePoolId(orderTreasurePool.getId());
        out.setTreasureFrom(orderTreasurePool.getItemType());
      } else {
        out.setSTreasureId(first.getSTreasureId());
        out.setOrderTreasurePoolId(-1L);
        out.setTreasureFrom(0);
      }
      out.setDdcUrl(sysUserBackpack.getDdcUrl());
      out.setDdcId(sysUserBackpack.getDdcId());
      out.setTransationId(sysUserBackpack.getTransationId());
      out.setOrderFingerprint(param.getOrderFingerprint());
      out.setBeforeUserId(userData.getId());
      out.setAfterUserId(sysUser.getId());
      out.setFinType(1);
      out.setCreateId(userData.getId());
      out.setCreateTime(new Date());
      sysUserBackpackDao.insert(out);

    } else {
      if (Strings.isEmpty(param.getPass())) {
        throw new RuntimeException("寄售藏品，操作密码必填");
      } else {
        SysUser sysUser = sysUserDao.selectById(userData.getId());
        if (Strings.isEmpty(sysUser.getOperationPass())) {
          throw new RuntimeException("用户暂未设置交易密码");
        } else if (!sysUser.getOperationPass().equals(DigestUtils.md5Hex(param.getPass()))) {
          throw new RuntimeException("对不起操作密码不正确");
        }
      }

      if (Strings.isEmpty(param.getOrderFingerprint()) || param.getPrice() == null) {
        throw new RuntimeException("订单指纹及价格不可为空");
      }
      // 出售藏品
      // 藏品是否出售由聚合池上品是否由该用户创建，订单指纹是否一致判定
      String turn_state = sysDictonaryDao.selectByAlias("turn_state");
      if (!turn_state.equals("1")) {
        throw new RuntimeException("流转中心暂未开启，无法寄售");
      }
      if (param.getPrice() == null) {
        throw new RuntimeException("出售藏品，价格必填");
      }

      //      String turn_max_price = sysDictonaryDao.selectByAlias("turn_max_price");
      //      if (param.getPrice().compareTo(new BigDecimal(turn_max_price)) > 0) {
      //        throw new RuntimeException("寄售藏品最大价格：" + turn_max_price);
      //      }
      //      String turn_min_price = sysDictonaryDao.selectByAlias("turn_min_price");
      //      if (param.getPrice().compareTo(new BigDecimal(turn_min_price)) < 0) {
      //        throw new RuntimeException("寄售藏品最小价格：" + turn_min_price);
      //      }

      List<SysUserBackpack> sysUserBackpacks =
          sysUserBackpackDao.selectByOrderFigerprintAndUserId(
              param.getOrderFingerprint(), userData.getId());
      SysUserBackpack sysUserBackpack = null;
      SysUserBackpack first = sysUserBackpacks.get(0);
      if (first.getFinType() == 1 || first.getFinType() == 3 || first.getFinType() == 7) {
        throw new RuntimeException("该藏品已不存在您的账户下");
      } else {
        sysUserBackpack = first;
      }
      LambdaQueryWrapper<StoreTreasure> que = Wrappers.lambdaQuery();
      que.eq(StoreTreasure::getOrderFingerprint, sysUserBackpack.getOrderFingerprint())
          .eq(BaseEntity::getCreateId, userData.getId())
          .orderByDesc(BaseEntity::getCreateTime);
      List<StoreTreasure> storeTreasures = storeTreasureDao.selectList(que);
      if (storeTreasures.size() == 0) {
        StoreTreasure storeTreasure = storeTreasureDao.selectById(sysUserBackpack.getSTreasureId());
        StoreTreasureRecord storeTreasureRecord =
            storeTreasureRecordDao.selectByOrderFing(sysUserBackpack.getOrderFingerprint());
        StoreTreasure plateTre = storeTreasureDao.selectById(storeTreasureRecord.getStrId());
        if (plateTre == null) {
          throw new RuntimeException("对不起订单系统繁忙，暂无法寄售，请稍后再试");
        }
        if (plateTre.getCouldSale() != 1) {
          throw new RuntimeException("该藏品暂无法寄售");
        }
        if (storeTreasure.getTreasureTitle().contains("测试")) {
          throw new RuntimeException("测试藏品暂无法寄售");
        }
        if (plateTre.getTurnMaxPrice() != null
            && param.getPrice().compareTo(plateTre.getTurnMaxPrice()) > 0) {
          throw new RuntimeException("寄售藏品最大价格：" + plateTre.getTurnMaxPrice());
        }
        if (plateTre.getTurnMinPrice() != null
            && param.getPrice().compareTo(plateTre.getTurnMinPrice()) < 0) {
          throw new RuntimeException("寄售藏品最小价格：" + plateTre.getTurnMinPrice());
        }

        storeTreasure.setId(null);
        if (storeTreasureRecord != null) {
          StoreTreasure trea = storeTreasureDao.selectById(storeTreasureRecord.getStrId());
          storeTreasure.setTotalCount(1);
          storeTreasure.setTNum(storeTreasureRecord.getStrNum());
        }
        storeTreasure.setFromUser(1);
        if (storeTreasure.getTType() == 2) {
          storeTreasure.setTType(0);
          String head = storeTreasure.getHeadImgPath();
          String indexImgPath = storeTreasure.getIndexImgPath();
          storeTreasure.setHeadImgPath(indexImgPath);
          storeTreasure.setIndexImgPath(head);
        } else if (storeTreasure.getTType() == 3 || storeTreasure.getTType() == 4) {
          storeTreasure.setTType(0);
          storeTreasure.setHeadImgPath(storeTreasure.getHeadImgPath());
          storeTreasure.setIndexImgPath(storeTreasure.getIndexImgPath());
        }
        storeTreasure.setSurplusCount(1);
        storeTreasure.setFrostCount(0);
        storeTreasure.setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
        storeTreasure.setTransationId(sysUserBackpack.getTransationId());
        storeTreasure.setLinkInfo(sysUserBackpack.getTransationId());
        storeTreasure.setCreateTime(new Date());
        storeTreasure.setCreateId(sysUserBackpack.getUId());
        storeTreasure.setPrice(param.getPrice());
        storeTreasure.setState(1);
        storeTreasure.setIsSale(0);
        if (storeTreasure.getUpTime() != null
            && storeTreasure.getUpTime().getTime() > System.currentTimeMillis()) {
          storeTreasure.setUpTime(new Date());
        }
        if (storeTreasure.getFromUser() == 1) {
          storeTreasure.setCouldSale(1);
        }

        storeTreasureDao.insert(storeTreasure);
      } else {
        if (storeTreasures.get(0).getCouldSale() != 1) {
          throw new RuntimeException("该藏品暂无法寄售");
        }
        if (storeTreasures.get(0).getTreasureTitle().contains("测试")) {
          throw new RuntimeException("测试藏品暂无法寄售");
        }
        StoreTreasureRecord storeTreasureRecord =
            storeTreasureRecordDao.selectByOrderFing(sysUserBackpack.getOrderFingerprint());
        StoreTreasure plateTre = storeTreasureDao.selectById(storeTreasureRecord.getStrId());
        if (plateTre.getTurnMaxPrice() != null
            && param.getPrice().compareTo(plateTre.getTurnMaxPrice()) > 0) {
          throw new RuntimeException("寄售藏品最大价格：" + plateTre.getTurnMaxPrice());
        }
        if (plateTre.getTurnMinPrice() != null
            && param.getPrice().compareTo(plateTre.getTurnMinPrice()) < 0) {
          throw new RuntimeException("寄售藏品最小价格：" + plateTre.getTurnMinPrice());
        }
        storeTreasures.get(0).setFromUser(1);
        storeTreasures.get(0).setSurplusCount(1);
        storeTreasures.get(0).setFrostCount(0);
        storeTreasures.get(0).setOrderFingerprint(sysUserBackpack.getOrderFingerprint());
        storeTreasures.get(0).setTransationId(sysUserBackpack.getTransationId());
        storeTreasures.get(0).setCreateTime(new Date());
        storeTreasures.get(0).setCreateId(sysUserBackpack.getUId());
        storeTreasures.get(0).setPrice(param.getPrice());
        storeTreasures.get(0).setUpdateTime(new Date());
        storeTreasures.get(0).setState(1);
        storeTreasures.get(0).setIsSale(0);
        if (storeTreasures.get(0).getFromUser() == 1) {
          storeTreasures.get(0).setCouldSale(1);
        }
        if (storeTreasures.get(0).getUpTime() != null
            && storeTreasures.get(0).getUpTime().getTime() > System.currentTimeMillis()) {
          storeTreasures.get(0).setUpTime(new Date());
        }
        storeTreasureDao.updateById(storeTreasures.get(0));
      }
    }
    return "SUCCESS";
  }

  @Autowired private StoreTreasureDao storeTreasureDao;
  //  @Autowired private StoreProPoolDao storeProPoolDao;

  public String updateUserName(String userName, UserDto userData) {
    SysUser sysUser = sysUserDao.selectById(userData.getId());
    sysUser.setUsername(userName);
    sysUserDao.updateById(sysUser);
    return "SUCCESS";
  }

  public String updateHeadImg(SysUser sysuser, UserDto userData) {
    SysUser sysUser = sysUserDao.selectById(userData.getId());
    sysUser.setHeadImg(sysuser.getHeadImg());
    sysUserDao.updateById(sysUser);
    return "SUCCESS";
  }

  public String updateOperationPass(String phone, String code, String pass, Long id) {
    String s = redisTemplate.opsForValue().get(phone);
    if (code.equals(s)) {
      SysUser sysUser = sysUserDao.selectById(id);
      sysUser.setOperationPass(DigestUtils.md5Hex(pass));
      sysUserDao.updateById(sysUser);
      return "SUCCESS";
    }
    throw new RuntimeException("验证码不正确");
  }

  public String updateTrue(SysUser param, UserDto userData) {
    SysUser sysUser = sysUserDao.selectById(userData.getId());
    if (sysUser.getIsTrue() != null && "1".equals(sysUser.getIsTrue())) {
      throw new RuntimeException("无需重复认证");
    } else {

      if (!sysUser.getPhone().equals(param.getPhone()))
        throw new RuntimeException("认证手机号需与当前登录手机号一致");
      LambdaQueryWrapper<SysUser> q = Wrappers.lambdaQuery(SysUser.class);
      q.eq(SysUser::getIdCard, param.getIdCard()).eq(SysUser::getIsTrue, 1);
      List<SysUser> sysUsers = sysUserDao.selectList(q);
      if (sysUsers.size() > 0) throw new RuntimeException("该身份证号已认证");
      //      JSONObject firstInfo = ALiTrueCheck.getInfo(param.getIdFirstPath());
      //      Boolean secInfo = ALiTrueCheck.checkSec(param.getIdSecPath());
      //      if (Strings.isNotEmpty(firstInfo.getString("PERSON_NAME"))) {
      //        if (!firstInfo.getString("PERSON_NAME").equals(param.getRealName())) {
      //          throw new RuntimeException("填写信息与图片识别信息不一致");
      //        }
      //      } else {
      //        throw new RuntimeException("未识别到有效信息");
      //      }
      //      if (Strings.isNotEmpty(firstInfo.getString("PERSON_ID"))) {
      //        if (!firstInfo.getString("PERSON_ID").equals(param.getIdCard())) {
      //          throw new RuntimeException("填写信息与图片识别信息不一致");
      //        }
      //      } else {
      //        throw new RuntimeException("未识别到有效信息");
      //      }

      if (ALiTrueCheck.checkFourParam(
          param.getIdCard(), param.getRealName(), param.getPhone(), param.getBankCard())) {
        SysUser inviteUser = null;
        if (param.getInviteCode() != null && !"".equals(param.getInviteCode())) {
          LambdaQueryWrapper<SysUser> qe = new LambdaQueryWrapper<>();
          qe.eq(SysUser::getUserIndex, param.getInviteCode());
          List<SysUser> inviteUsers = userDao.selectList(qe);
          if (inviteUsers.size() != 0) {
            // 添加邀请积分
            inviteUser = inviteUsers.get(0);
          } else {
            throw new RuntimeException("邀请码不正确，请核对");
          }
        }
        if (inviteUser != null) {
          String integer = sysDictonaryDao.selectByAlias("invite_user_integer");
          inviteUser.setMetaCount(
              new BigDecimal(inviteUser.getMetaCount()).add(new BigDecimal(integer)).longValue());
          userDao.updateById(inviteUser);
          ConIntegralRecord conIntegralRecord = new ConIntegralRecord();
          conIntegralRecord.setUId(inviteUser.getId());
          conIntegralRecord.setRecordType(1);
          conIntegralRecord.setMetaCount(Integer.valueOf(integer));
          conIntegralRecord.setOrderId(userData.getId());
          conIntegralRecord.setCreateId(inviteUser.getId());
          conIntegralRecord.setCreateTime(new Date());
          conIntegralRecordDao.insert(conIntegralRecord);
          SysUserInviteLink sysUserInviteLink = new SysUserInviteLink();
          sysUserInviteLink.setUId(inviteUser.getId());
          sysUserInviteLink.setSeUId(userData.getId());
          sysUserInviteLink.setIntegralCount(Long.valueOf(integer));
          sysUserInviteLinkDao.insert(sysUserInviteLink);
        }
        sysUser.setIdFirstPath(param.getIdFirstPath());
        sysUser.setIdSecPath(param.getIdSecPath());
        sysUser.setRealName(param.getRealName());
        sysUser.setIdCard(param.getIdCard());
        sysUser.setIsTrue("1");
        sysUserDao.updateById(sysUser);
        return "SUCCESS";
      } else {
        throw new RuntimeException("认证失败，请核对后再试");
      }
    }
  }

  @Autowired SysUserAdminDao sysUserAdminDao;

  public UserAdminInfo checkAdminLogin(LoginParam param) {
    SysUserAdmin sysUsers =
        sysUserAdminDao.selectListByNameAndPassword(
            param.getAdminAccount(),
            EncryptUtil.getInstance().MD5(param.getPassword(), passwordKey));
    if (sysUsers != null) {
      if (sysUsers.getState() == 0) {
        throw new RuntimeException("对不起账户已被冻结");
      } else {
        sysUsers.setAdminPass(null);
        String token = jwtTokenUtil.generateToken(sysUsers, null);
        redisTemplate
            .opsForValue()
            .set(token, JSONObject.toJSONString(sysUsers), 30, TimeUnit.DAYS);
        Subject subject = SecurityUtils.getSubject();
        subject.login(new JWTToken(token));

        UserAdminInfo result = new UserAdminInfo();
        result.setUser(sysUsers);
        result.setToken(token);
        List<Map<String, Object>> dynamicRoutingList =
            menuService.getDynamicRoutingList(sysUsers.getId());
        result.setMenusList(dynamicRoutingList);
        String all_token = redisTemplate.opsForValue().get("all_admin_token");
        if (Strings.isNotEmpty(all_token)) {
          JSONObject jsonObject = JSONObject.parseObject(all_token);
          String oldToken = jsonObject.getString(param.getAdminAccount());
          if (oldToken != null && !"".equals(oldToken)) {
            Boolean delete = redisTemplate.delete(oldToken);
            log.info("删除原token:" + delete + " token: \n" + oldToken);
          }
          jsonObject.put(param.getAdminAccount(), token);
          redisTemplate.opsForValue().set("all_admin_token", jsonObject.toJSONString());
        } else {
          JSONObject jsonObject = new JSONObject();
          jsonObject.put(param.getAdminAccount(), token);
          redisTemplate.opsForValue().set("all_admin_token", jsonObject.toJSONString());
        }
        return result;
      }
    }

    throw new RuntimeException("账户或密码不正确");
  }

  public SysUserAdmin selectAdminDataById(Long userId) {
    return sysUserAdminDao.selectById(userId);
  }

  public ApiResponse<Object> checkUserAccount(SysUserAdmin user) {
    ApiResponse<Object> success = ApiResponse.success();
    success.setCode(0);
    success.setMsg("");
    LambdaQueryWrapper<SysUserAdmin> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SysUserAdmin::getAdminAccount, user.getAdminAccount());
    if (user.getId() != null) {
      queryWrapper.ne(SysUserAdmin::getId, user.getId());
    }
    List<SysUserAdmin> selectList = sysUserAdminDao.selectList(queryWrapper);
    if (selectList.size() > 0) {
      success.setCode(1);
      success.setMsg("管理员账户重复");
    }
    return success;
  }

  public void updateDataById(SysUser sysUser) {
    sysUserDao.updateById(sysUser);
  }

  public CellTotal getCellTotal() {
    List<SysUser> sysUsers = sysUserDao.selectList(new LambdaQueryWrapper<>());
    CellTotal result = new CellTotal();
    List<CellResult> re = sysUserDao.getCollectLine();
    result.setTotalCount(new BigDecimal(sysUsers.size()));
    Collections.reverse(re);
    re = re.subList(0, Math.min(30, re.size()));
    Collections.reverse(re);
    result.setMonthCount(new BigDecimal(re.stream().mapToInt(o -> o.getCount().intValue()).sum()));
    result.setTodayCount(re.size() > 0 ? re.get(re.size() - 1).getCount() : new BigDecimal(0));
    result.setCellResultList(re);
    return result;
  }

  public List<SysUser> userinfoByUserIndex(String userIndex) {
    LambdaQueryWrapper<SysUser> que = new LambdaQueryWrapper<>();
    que.like(SysUser::getUserIndex, "%" + userIndex + "%");
    return sysUserDao.selectList(que);
  }

  @Autowired private RoleDao roleDao;

  public ApiResponse<Object> addRole(AddRoleParam role, SysUserAdminDto adminUserData) {
    Role sysRole = new Role();
    sysRole.setCreateId(adminUserData.getCreateId());
    sysRole.setCreateTime(new Date());
    sysRole.setName(role.getName());
    sysRole.setDescription(role.getDescription());
    sysRole.setSortNumber(role.getSortNumber());
    roleDao.insert(sysRole);
    return ApiResponse.success("success");
  }

  @Autowired private UMSign umSign;

  public MobileByTokenRes getMobileByToken(String token) {
    String userMobile = null;
    try {
      userMobile = umSign.getUserMobile(token);
    } catch (Exception e) {
      log.error("一键登录异常： " + e.getMessage());
    }
    if (userMobile != null) {
      SysUser user = sysUserDao.selectByMobile(userMobile);
      if (user == null) {
        MobileByTokenRes mobileByTokenRes = new MobileByTokenRes();
        mobileByTokenRes.setMobile(userMobile);
        mobileByTokenRes.setIsIn(false);
        mobileByTokenRes.setToken("");
        return mobileByTokenRes;
      } else {
        // 有
        if (user.getState() == 0) throw new RuntimeException("用户已被冻结");
        user.setPass(null);
        String tokens = jwtTokenUtil.generateToken(user, null);
        redisTemplate.opsForValue().set(tokens, JSONObject.toJSONString(user), 30, TimeUnit.DAYS);
        Subject subject = SecurityUtils.getSubject();
        subject.login(new JWTToken(tokens));
        MobileByTokenRes mobileByTokenRes = new MobileByTokenRes();
        mobileByTokenRes.setMobile(userMobile);
        mobileByTokenRes.setIsIn(true);
        mobileByTokenRes.setToken(tokens);
        return mobileByTokenRes;
      }
    }

    throw new RuntimeException("令牌失效");
  }

  public ApiResponse<Object> updateAdminPass(
      UpdateAdminParam updateAdminParam, SysUserAdminDto adminUserData) {
    SysUserAdmin sysUserAdmin = sysUserAdminDao.selectById(adminUserData.getId());
    if (sysUserAdmin == null) return ApiResponse.fail("无效的id");
    if (EncryptUtil.getInstance()
        .MD5(updateAdminParam.getOldPass(), passwordKey)
        .equals(sysUserAdmin.getAdminPass())) {
      sysUserAdmin.setAdminPass(
          EncryptUtil.getInstance().MD5(updateAdminParam.getNewPass(), passwordKey));
      sysUserAdminDao.updateById(sysUserAdmin);
      return ApiResponse.success();
    } else {
      return ApiResponse.fail("原密码不正确");
    }
  }

  public UserInfo openIdBind(BindThirdParam param) {
    SysUser sysUser = sysUserDao.selectByMobile(param.getMobile());
    if (sysUser == null) {
      // 注册用户
      LambdaQueryWrapper<SysUser> que = Wrappers.lambdaQuery();
      String uuid = null;
      String linkAddress = null;
      que.clear();
      que.eq(SysUser::getUserIndex, uuid);
      boolean flag = true;
      while (flag) {
        uuid = String.valueOf(IdUtil.getSnowflakeId()).substring(0, 6);
        que.clear();
        que.eq(SysUser::getUserIndex, uuid);
        List<SysUser> tmpUsers = userDao.selectList(que);
        if (tmpUsers.size() == 0) {
          flag = false;
        }
      }
      boolean flags = true;
      while (flags) {
        linkAddress = String.valueOf(IdUtil.getSnowflakeId());
        que.clear();
        que.eq(SysUser::getLinkAddress, linkAddress);
        List<SysUser> tmpUsers = userDao.selectList(que);
        if (tmpUsers.size() == 0) {
          flags = false;
        }
      }
      sysUser = new SysUser();
      switch (param.getType()) {
        case 0:
          sysUser.setAppleId(param.getOpenId());
          break;
        case 1:
          sysUser.setWxOpenId(param.getOpenId());
          break;
        case 2:
          sysUser.setQqOpenId(param.getOpenId());
          break;
        default:
          throw new RuntimeException("请指定绑定方式");
      }
      sysUser.setUserIndex(uuid);
      sysUser.setPhone(param.getMobile());
      sysUser.setCreateTime(new Date());
      sysUser.setLinkAddress(linkAddress);
      userDao.insert(sysUser);
    } else {
      // 绑定openId
      switch (param.getType()) {
        case 0:
          if (Strings.isNotEmpty(sysUser.getAppleId())) {
            throw new RuntimeException("此手机号已被绑定");
          }
          sysUser.setAppleId(param.getOpenId());
          break;
        case 1:
          if (Strings.isNotEmpty(sysUser.getWxOpenId())) {
            throw new RuntimeException("此手机号已被绑定");
          }
          sysUser.setWxOpenId(param.getOpenId());
          break;
        case 2:
          if (Strings.isNotEmpty(sysUser.getQqOpenId())) {
            throw new RuntimeException("此手机号已被绑定");
          }
          sysUser.setQqOpenId(param.getOpenId());
          break;
        default:
          throw new RuntimeException("请指定绑定方式");
      }
      sysUserDao.updateById(sysUser);
    }

    // 返回登录信息
    return loginBySysUser(sysUser);
  }
  // 根据openId登录
  public UserInfo loginByOpenId(BindThirdParam param) {
    LambdaQueryWrapper<SysUser> que = Wrappers.lambdaQuery();
    switch (param.getType()) {
      case 0:
        que.eq(SysUser::getAppleId, param.getOpenId());
        break;
      case 1:
        que.eq(SysUser::getWxOpenId, param.getOpenId());
        break;
      case 2:
        que.eq(SysUser::getQqOpenId, param.getOpenId());
        break;
      default:
        throw new RuntimeException("请指定登录方式");
    }
    List<SysUser> sysUsers = sysUserDao.selectList(que);
    if (sysUsers.size() > 0) {
      return loginBySysUser(sysUsers.get(0));
    } else {
      throw new RuntimeException("请绑定手机号");
    }
  }

  private UserInfo loginBySysUser(SysUser sysUser) {
    if (sysUser.getState() == 0) throw new RuntimeException("用户已被冻结");
    String token = jwtTokenUtil.generateToken(sysUser, null);
    redisTemplate.opsForValue().set(token, JSONObject.toJSONString(sysUser), 30, TimeUnit.DAYS);
    Subject subject = SecurityUtils.getSubject();
    subject.login(new JWTToken(token));

    UserInfo result = new UserInfo();
    sysUser.setIsVip(0);
    result.setUser(sysUser);
    result.setToken(token);
    return result;
  }

  public List<SysUser> getUserByIds(List<Long> collect) {
    if (collect.size() == 0) {
      return new ArrayList<>();
    } else {
      LambdaQueryWrapper<SysUser> que = Wrappers.lambdaQuery(SysUser.class);
      que.in(SysUser::getId, collect);
      return sysUserDao.selectList(que);
    }
  }

  @Autowired private ConBalanceRecordDao conBalanceRecordDao;
  @Autowired private ConRechargeDao conRechargeDao;
  @Autowired private ConWithdrawDao conWithdrawDao;
  @Autowired private OrderProductDao orderProductDao;
  @Autowired private OrderVipDao orderVipDao;
  /** 注销用户 */
  // TODO 登录时积分送重复了
  public void removeUser(UserDto userData, String token) {
    Object oldToken = redisTemplate.opsForHash().get("allToken", userData.getPhone());
    if (oldToken != null) {
      Boolean delete = redisTemplate.delete(oldToken.toString());
      redisTemplate.opsForHash().put("allToken", userData.getPhone(), token);
    }
    redisTemplate.delete(token);
    // 注销用户开始
    SysUser sysUser = sysUserDao.selectById(userData.getId());
    if (sysUser != null && sysUser.getState() == 1) {
      sysUser.setState(-1);
      sysUserDao.updateById(sysUser);
      // 清除账户订单（藏品，商品）订单
      orderTreasurePoolDao.delectByUId(sysUser.getId());
      orderProductDao.deleteByUId(sysUser.getId());
      // 清除积分记录
      conIntegralRecordDao.delectByUId(sysUser.getId());
      // 清除交易流水 充值 提现
      conBalanceRecordDao.delectByUId(sysUser.getId());
      conRechargeDao.deleteByUId(sysUser.getId());
      conWithdrawDao.deleteByUId(sysUser.getId());
      // 清除我的藏品
      sysUserBackpackDao.delectByUId(sysUser.getId());
      // 清除vipOrder
      orderVipDao.deleteByUId(sysUser.getId());
      // 删除邀请记录
      sysUserInviteLinkDao.deleteByUId(sysUser.getId());
    } else {
      throw new RuntimeException("账户异常，请重新登录后再试");
    }
  }

  public SysUser selectByPhone(String phone) {
    LambdaQueryWrapper<SysUser> que = Wrappers.lambdaQuery(SysUser.class);
    que.eq(SysUser::getPhone, phone);
    List<SysUser> sysUsers = sysUserDao.selectList(que);
    if (sysUsers.size() > 0) {
      return sysUsers.get(0);
    }
    return null;
  }

  @Autowired private SysBeforeUserDao sysBeforeUserDao;

  public IPage<UserDto> beforeSysUserPage(
      Class<UserDto> userDtoClass,
      Integer pageNum,
      long parseLong,
      PageSelectParam<SysUser> param,
      Long streId) {
    LambdaQueryWrapper<SysBeforeUser> queryWrapper = new LambdaQueryWrapper<>();
    if (param.getSelectParam().getPhone() != null) {
      queryWrapper.like(SysBeforeUser::getPhone, "%" + param.getSelectParam().getPhone() + "%");
    }
    queryWrapper.eq(SysBeforeUser::getStreaId, streId);
    Page<SysBeforeUser> sysBeforeUserPage =
        sysBeforeUserDao.selectPage(new Page<>(1, 99999999), queryWrapper);
    Set<String> phones =
        sysBeforeUserPage.getRecords().stream()
            .map(SysBeforeUser::getPhone)
            .collect(Collectors.toSet());
    phones.add("-111");
    LambdaQueryWrapper<SysUser> que = Wrappers.lambdaQuery(SysUser.class);
    que.in(SysUser::getPhone, phones);

    IPage<UserDto> result =
        selectPageList(
            UserDto.class, param.getPageNum(), Long.parseLong(param.getPageSize().toString()), que);
    result
        .getRecords()
        .forEach(
            o -> {
              List<SysBeforeUser> tmpUsers =
                  sysBeforeUserPage.getRecords().stream()
                      .filter(t -> t.getPhone().equals(o.getPhone()))
                      .collect(Collectors.toList());
              if (tmpUsers.size() > 0) {
                o.setRuleCount(tmpUsers.get(0).getRuleCount());
                o.setBeId(tmpUsers.get(0).getId());
              }
              MyTreasure noCheck = new MyTreasure();
              noCheck.setIsCheck(0);
              UserDto userData = new UserDto();
              userData.setId(o.getId());
              List<MyTreasure> mytreasure = storeProPoolService.getMytreasure(noCheck, userData);
              long count =
                  mytreasure.stream().filter(t -> streId.equals(t.getSTreasureId())).count();
              MyTreasure noCheck1 = new MyTreasure();
              noCheck1.setIsCheck(1);
              List<MyTreasure> mytreasures = storeProPoolService.getMytreasure(noCheck1, userData);
              long count2 =
                  mytreasures.stream().filter(t -> streId.compareTo(t.getId()) == 0).count();
              o.setHasCount(new Integer(count + count2 + ""));
            });
    return result;
  }

  public void addBeforeUser(List<String> phones, Long streId, Long count) {
    LambdaQueryWrapper<SysBeforeUser> q = Wrappers.lambdaQuery(SysBeforeUser.class);
    q.eq(SysBeforeUser::getStreaId, streId);
    List<SysBeforeUser> sysBeforeUsers = sysBeforeUserDao.selectList(q);
    Set<String> allPhones =
        sysBeforeUsers.stream().map(SysBeforeUser::getPhone).collect(Collectors.toSet());
    Set<String> needAdd =
        phones.stream().filter(o -> !allPhones.contains(o)).collect(Collectors.toSet());
    needAdd.forEach(
        o -> {
          SysBeforeUser sysBeforeUser = new SysBeforeUser();
          sysBeforeUser.setState(1);
          sysBeforeUser.setPhone(o);
          sysBeforeUser.setStreaId(streId);
          sysBeforeUser.setRuleCount(count.intValue());

          sysBeforeUser.setCreateTime(new Date());
          sysBeforeUserDao.insert(sysBeforeUser);
        });
  }

  public void deleteBeforeUser(List<String> phones, Long streId) {

    LambdaQueryWrapper<SysBeforeUser> que = Wrappers.lambdaQuery(SysBeforeUser.class);
    que.in(SysBeforeUser::getPhone, phones);
    que.eq(SysBeforeUser::getStreaId, streId);
    sysBeforeUserDao.delete(que);
  }

  public List<SysUser> findUserByTreaId(List<Long> ids) {

    //    LambdaQueryWrapper<SysUserBackpack> que = Wrappers.lambdaQuery(SysUserBackpack.class);
    //    que.in(SysUserBackpack::getSTreasureId, ids);
    //    List<SysUserBackpack> allBack = sysUserBackpackDao.selectList(que);
    //    Map<Long, Set<Long>> userHas = new HashMap<>();
    //    for (Long userId :
    // allBack.stream().map(SysUserBackpack::getUId).collect(Collectors.toSet())) {
    //      List<SysUserBackpack> sysUserBackpacks =
    //          allBack.stream().filter(o ->
    // o.getUId().equals(userId)).collect(Collectors.toList());
    //      // 全部的
    //      List<SysUserBackpack> all =
    //          sysUserBackpacks.stream()
    //              .filter(
    //                  o ->
    //                      o.getFinType() == 2
    //                          || o.getFinType() == 4
    //                          || o.getFinType() == 6
    //                          || o.getFinType() == 8)
    //              .collect(Collectors.toList());
    //      // 售出赠送的 消耗
    //      List<SysUserBackpack> out =
    //          sysUserBackpacks.stream()
    //              .filter(o -> o.getFinType() == 1 || o.getFinType() == 3 || o.getFinType() == 7)
    //              .collect(Collectors.toList());
    //      for (SysUserBackpack sysUserBackpack : out) {
    //        int index = -1;
    //        for (int i = 0; i < all.size(); i++) {
    //          if (all.get(i).getOrderFingerprint().equals(sysUserBackpack.getOrderFingerprint()))
    // {
    //            index = i;
    //          }
    //        }
    //        if (index != -1) all.remove(index);
    //      }
    //      if (all.size() > 0) {
    //        userHas.put(
    //            userId,
    // all.stream().map(SysUserBackpack::getSTreasureId).collect(Collectors.toSet()));
    //      }
    //    }
    //    // 查询持有某些藏品的用户列表
    //    List<Long> userIds = new ArrayList<>();
    //    for (Long userId : userHas.keySet()) {
    //      if (userHas.get(userId) != null) {
    //        if (userHas.get(userId).size() == ids.size()) {
    //          userIds.add(userId);
    //        }
    //      }
    //    }
    //    userIds.add(-1111L);
    //    List<SysUser> result = sysUserDao.selectBatchIds(userIds);
    //    result.forEach(
    //        o -> {
    //          MyTreasure noCheck = new MyTreasure();
    //          noCheck.setIsCheck(0);
    //          UserDto userData = new UserDto();
    //          userData.setId(o.getId());
    //          List<MyTreasure> mytreasure = storeProPoolService.getMytreasure(noCheck, userData);
    //          long count = mytreasure.stream().filter(t ->
    // ids.contains(t.getSTreasureId())).count();
    //          MyTreasure noCheck1 = new MyTreasure();
    //          noCheck1.setIsCheck(1);
    //          List<MyTreasure> mytreasures = storeProPoolService.getMytreasure(noCheck1,
    // userData);
    //          long count2 = mytreasures.stream().filter(t -> ids.contains(t.getId())).count();
    //          o.setHasCount(new Integer(count + count2 + ""));
    //        });
    OrderTreaCopyParam param = new OrderTreaCopyParam();
    param.setStreaIds(ids);
    IPage<SysUser> page = orderTreasurePoolService.orderTreaCopy(new Page(1, 999999999), param);
    page.getRecords().forEach(o -> o.setFlag(1));
    List<SysUser> result = page.getRecords();

    return result;
  }

  @Autowired private StoreProPoolService storeProPoolService;

  public List<SysUser> findByParam(LambdaQueryWrapper<SysUser> que) {
    return sysUserDao.selectList(que);
  }

  public void approve(String userId) {
    log.info("用户授权开始，userID: " + userId);
    SysUser sysUser = sysUserDao.selectById(userId);
    try {
      DDCSdkClient client =
          new DDCSdkClient.Builder()
              .setAuthorityLogicAddress("0xFa1d2d3EEd20C4E4F5b927D9730d9F4D56314B29")
              .setChargeLogicAddress("0x0B8ae0e1b4a4Eb0a0740A250220eE3642d92dc4D")
              .setDDC721Address("0x354c6aF2cB870BEFEA8Ea0284C76e4A46B8F2870")
              .setDDC1155Address("0x0E762F4D11439B1130D402995328b634cB9c9973")
              .setGasLimit("35000")
              .setGasPrice("1")
              .setSignEventListener(new UserSignEvent(sysUser.getPrivateKey()))
              .init();
      client
          .getDDC1155Service()
          .setApprovalForAll(
              sysUser.getLinkAddress(), ResourcesUtil.getProperty("bsn_plate_addr"), true);
      log.info("授权完成等待校验userId: " + userId + " 用户信息：" + JSONObject.toJSONString(sysUser));
      return;
    } catch (Exception e) {
      log.info("用户授权失败，重新充值用户能量值");
      //      WenchangDDC bsnUtil = new WenchangDDC();
      //      SysUser sysUser1 = bsnUtil.userAddGas(sysUser.getLinkAddress(), sysUser);
      //      if (sysUser1 != null) {
      //        sysUserDao.updateById(sysUser1);
      //      }
      throw new RuntimeException(e);
    }
  }

  public void checkApprove(String userId) {
    log.info("用户授权校验开始，userID: " + userId);
    SysUser sysUser = sysUserDao.selectById(userId);
    try {
      DDCSdkClient client =
          new DDCSdkClient.Builder()
              .setAuthorityLogicAddress("0xFa1d2d3EEd20C4E4F5b927D9730d9F4D56314B29")
              .setChargeLogicAddress("0x0B8ae0e1b4a4Eb0a0740A250220eE3642d92dc4D")
              .setDDC721Address("0x354c6aF2cB870BEFEA8Ea0284C76e4A46B8F2870")
              .setDDC1155Address("0x0E762F4D11439B1130D402995328b634cB9c9973")
              .setGasLimit("35000")
              .setGasPrice("1")
              .setSignEventListener(new UserSignEvent(sysUser.getPrivateKey()))
              .init();
      Boolean bsn_plate_addr =
          client
              .getDDC1155Service()
              .isApprovedForAll(
                  sysUser.getLinkAddress(), ResourcesUtil.getProperty("bsn_plate_addr"));
      if (!bsn_plate_addr) {
        RBlockingQueue<Object> blockingFairQueue =
            redissonClient.getBlockingQueue("delay_queue_call");
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer("user_approve_" + sysUser.getId(), 2, TimeUnit.MINUTES);
        log.info("授权校验未能通过，重新授权： userId: " + userId + " 用户信息：" + JSONObject.toJSONString(sysUser));
      } else {
        log.info("授权校验通过userId: " + userId + " 用户信息：" + JSONObject.toJSONString(sysUser));
      }
      return;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String addImportUser(Set<Map<String, Object>> mobiles, Long streId, Long count) {
    LambdaQueryWrapper<SysUser> que = Wrappers.lambdaQuery(SysUser.class);
    List<SysUser> allUser = sysUserDao.selectList(que);
    Set<String> allUserPhone = allUser.stream().map(SysUser::getPhone).collect(Collectors.toSet());
    Set<String> needRegPhone =
        mobiles.stream()
            .filter(o -> !allUserPhone.contains(o.get("mobile").toString()))
            .map(o -> o.get("mobile").toString())
            .collect(Collectors.toSet());
    LambdaQueryWrapper<SysBeforeUser> q = Wrappers.lambdaQuery(SysBeforeUser.class);
    q.eq(SysBeforeUser::getStreaId, streId);
    List<SysBeforeUser> allBefore = sysBeforeUserDao.selectList(q);
    Set<String> allBeforeUser =
        allBefore.stream().map(SysBeforeUser::getPhone).collect(Collectors.toSet());
    Set<Map<String, Object>> needAdd =
        mobiles.stream()
            .filter(
                o ->
                    !needRegPhone.contains(o.get("mobile").toString())
                        && !allBeforeUser.contains(o.get("mobile").toString()))
            .collect(Collectors.toSet());
    if (needAdd.size() > 0) {
      for (Map<String, Object> tmpAdd : needAdd) {
        addBeforeUser(
            Collections.singletonList(tmpAdd.get("mobile").toString()),
            streId,
            tmpAdd.get("ruleCount") == null
                ? 1
                : Long.parseLong(tmpAdd.get("ruleCount").toString()));
      }
      String addMobile = Strings.join(needAdd, ',');
      String needReg = Strings.join(needRegPhone, ',');
      return "如下用户添加成功："
          + addMobile
          + (Strings.isNotEmpty(needReg) ? "下列用户暂未注册：" + Strings.join(needRegPhone, ',') : "");
    } else {
      throw new RuntimeException(
          "可添加用户均已在优先购名单中"
              + (needRegPhone.size() > 0 ? " 如下用户需注册" + Strings.join(needRegPhone, ',') : ""));
    }
  }

  public void updateRuleCount(UpdateRuleParam param) {
    SysBeforeUser sysBeforeUser = sysBeforeUserDao.selectById(param.getBeId());
    if (sysBeforeUser != null) {
      sysBeforeUser.setRuleCount(param.getRuleCount());
      sysBeforeUserDao.updateById(sysBeforeUser);
    } else {
      throw new RuntimeException("id异常");
    }
  }

  public void updateUserOpening(String mobile) {
    List<SysUser> sysUsers = sysUserDao.selectByPhone(mobile);
    if (sysUsers.size() > 0) {
      sysUsers.get(0).setIsOpening(1);
      sysUserDao.updateById(sysUsers.get(0));
    }
  }
}
