package com.qy.ntf.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.StoreTreasure;
import com.qy.ntf.bean.entity.StoreTreasureRecord;
import com.qy.ntf.bean.param.AddCouldCompParam;
import com.qy.ntf.bean.param.BindBoxAddParam;
import com.qy.ntf.bean.param.TreaNeedParam;
import com.qy.ntf.bean.param.TurnParam;
import com.qy.ntf.config.AccessLimit;
import com.qy.ntf.dao.AvataRecordDao;
import com.qy.ntf.dao.StoreTreasureRecordDao;
import com.qy.ntf.service.StoreTreasureService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品首发/超前申购 主体 控制层接口
 */
@Api(tags = {"藏品首发、超前申购"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/storeTreasure")
public class StoreTreasureController extends BaseController {
  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 藏品首发/超前申购 主体 服务 */
  @Autowired private StoreTreasureService storeTreasureService;

  @ApiOperation(value = "ADMIN - " + "藏品首发、超前申购 主体添加-erc721-需填写编号", notes = "添加")
  @PostMapping("/add")
  @AccessLimit(times = 1, second = 1)
  public ApiResponse<Void> add(
      @RequestBody @Valid StoreTreasureAddDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      setCreateUserInfo(dto);
      dto.setRType(0);
      if (dto.getTType() == 0) {
        if (dto.getSysSeriesId() == null) {
          throw new RuntimeException("首发藏品，缺少系列id");
        }
        dto.setSysOrgId(-1L);
      }
      checkStreaTime(dto.getTType(), dto.getUpTime(), dto.getDownTime(), dto.getCheckTime());
      storeTreasureService.save(dto);
    }
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "藏品首发、超前申购 主体添加-erc1155-无需填写编号-需说明添加数量", notes = "添加")
  @PostMapping("/batchAdd")
  @AccessLimit(times = 1, second = 1)
  public ApiResponse<Void> batchAdd(
      @RequestBody @Valid StoreTreasureAddDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      setCreateUserInfo(dto);
      dto.setRType(1);
      dto.setTType(0);
      dto.setSurplusCount(dto.getTotalCount());
      storeTreasureService.batchAdd(dto);
      return ApiResponse.success();
    }
  }

  @Autowired private RedissonClient redissonClient;

  @ApiOperation(value = "ADMIN - " + "盲盒添加", notes = "盲盒添加")
  @PostMapping("/bindBoxAdd")
  @AccessLimit(times = 1, second = 1)
  public ApiResponse<Void> bindBoxAdd(
      @RequestBody @Valid BindBoxAddParam dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      storeTreasureService.bindBoxAdd(dto, getAdminUserData());
    }
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "盲盒修改", notes = "修改")
  @PostMapping("/bindBoxUpdate")
  @AccessLimit(times = 1, second = 1)
  public ApiResponse<Void> bindBoxUpdate(
      @RequestBody @Valid BindBoxAddParam dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      RLock rLock = redissonClient.getLock("LOCK_bindBox_" + dto.getId());

      try {
        if (rLock.tryLock(0, 3, TimeUnit.SECONDS)) {

          storeTreasureService.bindBoxUpdate(dto, getAdminUserData());
          if (rLock.isLocked()) {
            if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
              rLock.unlock(); // 释放锁
            }
          }
          return ApiResponse.success();
        }
      } catch (Exception e) {
        if (rLock.isLocked()) {
          if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
            rLock.unlock(); // 释放锁
          }
        }
        throw new RuntimeException(e);
      }
    }
    return ApiResponse.fail("异常");
  }
  // 盲盒下藏品列表
  @ApiOperation(value = "ADMIN - " + "盲盒下藏品列表")
  @PostMapping("/bindBoxItemByPage")
  public ApiResponse<IPage<StoreTreasureDto>> bindBoxItemByPage(
      @RequestBody PageSelectParam<Long> param) {
    IPage<StoreTreasureDto> page = storeTreasureService.bindBoxItemByPage(param);
    for (StoreTreasureDto record : page.getRecords()) {
      record.setLinkInfo(record.getTransationId());
    }
    return ApiResponse.success(page);
  }
  // 添加盲盒下藏品
  @ApiOperation(value = "ADMIN - " + "添加盲盒下藏品", notes = "添加")
  @PostMapping("/bindBoxItemAdd")
  @AccessLimit(times = 1, second = 1)
  public ApiResponse<Void> bindBoxItemAdd(
      @RequestBody @Valid BindBoxItemAddDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      storeTreasureService.bindBoxItemAdd(dto, getAdminUserData());
    }
    return ApiResponse.success();
  }
  // 修改盲盒下藏品
  @ApiOperation(value = "ADMIN - " + "编辑盲盒下藏品", notes = "编辑盲盒下藏品")
  @PostMapping("/bindBoxItemUpdate")
  @AccessLimit(times = 1, second = 1)
  public ApiResponse<Void> bindBoxItemUpdate(
      @RequestBody @Valid BindBoxItemAddDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      storeTreasureService.bindBoxItemUpdate(dto, getAdminUserData());
    }
    return ApiResponse.success();
  }

  // admin 列表
  @ApiOperation(value = "ADMIN - " + "盲盒列表")
  @PostMapping("/bindBoxByPage")
  public ApiResponse<IPage<StoreTreasureDto>> bindBoxByPage(
      @RequestBody PageSelectParam<StoreTreasureSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreTreasure> queryWrapper = new LambdaQueryWrapper<>();
    if (Strings.isNotEmpty(param.getSelectParam().getTreasureTitle())) {
      queryWrapper.like(StoreTreasure::getTreasureTitle, param.getSelectParam().getTreasureTitle());
    }
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    queryWrapper.eq(StoreTreasure::getTType, 3);
    queryWrapper.ne(BaseEntity::getState, -1);

    IPage<StoreTreasureDto> page =
        storeTreasureService.getBindListByPage(
            StoreTreasureDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP - " + "盲盒列表")
  @PostMapping("/bindBoxByAppPage")
  public ApiResponse<IPage<StoreTreasureDto>> bindBoxByAppPage(
      @RequestBody PageSelectParam<StoreTreasureSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreTreasure> queryWrapper = new LambdaQueryWrapper<>();
    if (Strings.isNotEmpty(param.getSelectParam().getTreasureTitle())) {
      queryWrapper.like(StoreTreasure::getTreasureTitle, param.getSelectParam().getTreasureTitle());
    }
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    queryWrapper.eq(StoreTreasure::getTType, 3);
    queryWrapper.ne(BaseEntity::getState, -1);

    IPage<StoreTreasureDto> page =
        storeTreasureService.getAppBindListByPage(
            StoreTreasureDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper,
            getUserData());

    return ApiResponse.success(page);
  }
  // 上链
  @ApiOperation(value = "ADMIN - " + "上链操作", notes = "上链")
  @PostMapping("/joinBlod/{id}")
  public ApiResponse<String> joinBlod(@PathVariable("id") String id) {

    storeTreasureService.joinBlod(id);
    return ApiResponse.success();
  }

  @Autowired private StoreTreasureRecordDao storeTreasureRecordDao;
  @Autowired private AvataRecordDao avataRecordDao;

  @PostMapping("/joinBlodCallBack")
  public String joinBlodCallBack(HttpServletRequest request) {
    Map params = new HashMap<>();
    BufferedReader br = null;
    try {
      try {
        br = request.getReader();
      } catch (IOException e) {
        e.printStackTrace();
      }
      String str;
      StringBuilder wholeStr = new StringBuilder();
      while ((str = Objects.requireNonNull(br).readLine()) != null) {
        wholeStr.append(str);
      }
      if (StrUtil.isNotEmpty(wholeStr.toString())) {
        params = JSONObject.parseObject(wholeStr.toString(), Map.class);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    RLock rLock = redissonClient.getLock("LOCK_trea_" + (String) params.get("operation_id"));
    try {
      if (rLock.tryLock(0, 5, TimeUnit.SECONDS)) {
        System.out.println("++++++++++++++++avata回调");

        System.out.println("回调请求参数：" + params);
        if (!((String) params.get("type")).equals("mint_nft_batch")) {
          return "success";
        }
        String tmpTreaId = (String) params.get("operation_id");
        String storeTreaId = avataRecordDao.findStrIdByTmpId(tmpTreaId);
        StoreTreasure storeTreasure = storeTreasureService.selectById(Long.valueOf(storeTreaId));
        JSONObject nft = JSONObject.parseObject((String) params.get("nft"), JSONObject.class);
        if (storeTreasure.getAllNftId() == null) {
          storeTreasure.setAllNftId(nft.getString("nft_id"));
        } else if (!"".equals(storeTreasure.getAllNftId())) {
          storeTreasure.setAllNftId(storeTreasure.getAllNftId() + "," + nft.getString("nft_id"));
        }
        storeTreasure.setDdcId(nft.getString("nft_id").split(",")[0]);
        storeTreasure.setState(1);
        storeTreasureService.updateById(storeTreasure);
        Integer addedCount = storeTreasureRecordDao.getCountByStrId(Long.valueOf(storeTreaId));
        String[] nftIds = nft.getString("nft_id").split(",");
        for (int i = addedCount + 1; i < addedCount + nftIds.length + 1; i++) {
          StoreTreasureRecord storeTreasureRecord = new StoreTreasureRecord();
          storeTreasureRecord.setStrId(Long.valueOf(storeTreaId));
          storeTreasureRecord.setStrNum(
              String.format(
                  "%0" + ((storeTreasure.getTotalCount() + "").length() + "").length() + "d", i));
          storeTreasureRecord.setNftId(nftIds[i - addedCount - 1]);
          storeTreasureRecord.setCreateId(1L);
          storeTreasureRecord.setCreateTime(new Date());
          storeTreasureRecord.setState(1);
          storeTreasureRecordDao.insert(storeTreasureRecord);
        }
        System.out.println("++++++++++++++++avata回调结束---------");
        return "success";
      }
    } catch (Exception e) {
      if (rLock.isLocked()) {
        if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
          rLock.unlock(); // 释放锁
        }
      }
    }
    return "error";
  }
  // 已废弃
  @ApiOperation(value = "ADMIN - " + "上链操作", notes = "上链")
  @PostMapping("/bindBoxJoinBlod/{id}")
  @AccessLimit(times = 1, second = 600)
  public ApiResponse<String> bindBoxJoinBlod(@PathVariable("id") String id) {
    storeTreasureService.joinBlod(id);
    return ApiResponse.success();
  }
  // 延时获取ddcId url 获取成功则修正藏品状态

  @ApiOperation(value = "APP - " + "藏品首发、超前申购 主体详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<StoreTreasureDto> detail(@PathVariable("id") Long id) {
    StoreTreasureDto dto = storeTreasureService.getStoreTreasureById(id, getUserData());
    dto.setLinkInfo(dto.getTransationId());
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "ADMIN - " + "藏品首发、超前申购 主体修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(
      @RequestBody @Valid StoreTreasureUpdateDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      dto.setUpdateTime(new Date());
      dto.setTType(0);
      //      if (dto.getTType() == 0) {
      //        if (dto.getSysSeriesId() == null) {
      //          throw new RuntimeException("首发藏品，缺少系列id");
      //        }
      //        dto.setSysOrgId(-1L);
      //      }
      if (dto.getTotalCount() != null && dto.getTotalCount() < 0) {
        throw new RuntimeException("总数量不可小于0");
      }
      if (dto.getPrice() != null && dto.getPrice().doubleValue() < 0) {
        throw new RuntimeException("价格不可小于0");
      }
      if (dto.getZeroPrice() != null && dto.getZeroPrice().doubleValue() < 0) {
        throw new RuntimeException("价格不可小于0");
      }
      storeTreasureService.update(dto, getAdminUserData());
      return ApiResponse.success();
    }
  }

  private void checkStreaTime(Integer tType, Date upTime, Date downTime, Date checkTime) {
    if (tType == 1) {
      if (upTime != null) {
        if (downTime != null) {
          if (checkTime != null) {
            if (downTime.getTime() > upTime.getTime()) {
              if (checkTime.getTime() > downTime.getTime()) {
                throw new RuntimeException("抽奖时间要早于截止时间");
              }
            } else {
              throw new RuntimeException("开始时间要早于截止时间");
            }
          } else {
            throw new RuntimeException("开奖时间不能为空");
          }
        } else {
          throw new RuntimeException("截止时间不能为空");
        }
      } else {
        throw new RuntimeException("开始时间不能为空");
      }
    }
  }

  @ApiOperation(value = "ADMIN - " + "藏品首发、超前申购 主体修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody StoreTreasureDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreasureDto dto = new StoreTreasureDto();
    md.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    md.map(tmpDto, dto);
    dto.setUpdateTime(new Date());
    storeTreasureService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "藏品首发、超前申购 主体删除", notes = "主体删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    storeTreasureService.deleteById(id, getAdminUserData());
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "藏品首发、超前申购 主体分页列表-tType藏品类型0藏品首发1超前申购")
  @PostMapping("/page")
  public ApiResponse<IPage<StoreTreasureDto>> listByPage(
      @RequestBody PageSelectParam<StoreTreasureSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreTreasure> queryWrapper = new LambdaQueryWrapper<StoreTreasure>();
    ModelMapper md = new ModelMapper();
    md.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    StoreTreasureDto dto = new StoreTreasureDto();
    md.map(param.getSelectParam(), dto);
    if (dto.getTType() != null) {
      queryWrapper.eq(StoreTreasure::getTType, dto.getTType());
    } else {
      List<Integer> types = new ArrayList<>();
      types.add(0);
      types.add(1);
      types.add(2);
      //      types.add(4);
      queryWrapper.in(StoreTreasure::getTType, types);
    }
    queryWrapper.eq(StoreTreasure::getFromUser, 0);
    if (dto.getSysSeriesId() != null) {
      queryWrapper.eq(StoreTreasure::getSysSeriesId, dto.getSysSeriesId());
    }

    if (Strings.isNotEmpty(dto.getTreasureTitle())) {
      queryWrapper.like(StoreTreasure::getTreasureTitle, dto.getTreasureTitle());
    }

    if (param.getSelectParam().getEnCollectId() != null) {
      queryWrapper.ne(StoreTreasure::getId, param.getSelectParam().getEnCollectId());
    }
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    queryWrapper.ne(BaseEntity::getState, -1);

    IPage<StoreTreasureDto> page =
        storeTreasureService.getListByPage(
            StoreTreasureDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);
    for (StoreTreasureDto record : page.getRecords()) {
      record.setLinkInfo(record.getTransationId());
    }
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "优先购页面藏品列表")
  @PostMapping("/beforeUserpage")
  public ApiResponse<IPage<StoreTreasureDto>> beforeUserpage() {
    // 组合查询条件
    LambdaQueryWrapper<StoreTreasure> queryWrapper = new LambdaQueryWrapper<StoreTreasure>();
    List<Integer> types = new ArrayList<>();
    types.add(0);
    types.add(2);
    types.add(3);

    queryWrapper.in(StoreTreasure::getTType, types);
    queryWrapper.eq(StoreTreasure::getFromUser, 0);
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);

    IPage<StoreTreasureDto> page =
        storeTreasureService.getListByPage(StoreTreasureDto.class, 1, 999999999L, queryWrapper);
    for (StoreTreasureDto record : page.getRecords()) {
      record.setLinkInfo(record.getTransationId());
    }
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP - " + "流转中心列表")
  @PostMapping("/turnPage")
  public ApiResponse<IPage<StoreTreasureDto>> turnPage(
      @RequestBody PageSelectParam<TurnParam> param) {
    IPage<StoreTreasureDto> page = storeTreasureService.turnPage(param);
    for (StoreTreasureDto record : page.getRecords()) {
      record.setLinkInfo(record.getTransationId());
    }
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "合成藏品添加-erc721-需填写编号", notes = "添加")
  @PostMapping("/couldComAdd")
  public ApiResponse<Void> couldComAdd(
      @RequestBody @Valid StoreTreasureAddDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      if (dto.getNeededTreId() != null && dto.getNeededTreId().size() > 0) {
        if (Strings.isNotEmpty(dto.getTreasureTitle())) {
          if (dto.getUpTime() != null) {
            if (dto.getDownTime() != null) {
              List<TreaNeedParam> collect =
                  dto.getNeededTreId().stream()
                      .filter(o -> o.getId() == null)
                      .collect(Collectors.toList());
              if (collect.size() > 0) throw new RuntimeException("所需藏品id不能为空");
              setCreateUserInfo(dto);
              dto.setCouldCompound(1);
              dto.setTType(2);
              ArrayList<String> list = new ArrayList<>();
              list.add(dto.getDetail());
              list.add(dto.getIndexImgPath());
              list.add(dto.getHeadImgPath());
              dto.setHeadImgPath(list.get(0));
              dto.setDetail(list.get(1));
              dto.setIndexImgPath(list.get(2));
              dto.setState(3);
              storeTreasureService.save(dto);
              return ApiResponse.success();
            } else {
              throw new RuntimeException("开始时间 不可为空");
            }
          } else {
            throw new RuntimeException("结束时间 不可为空");
          }
        } else {
          throw new RuntimeException("藏品标题 不可为空");
        }

      } else {
        throw new RuntimeException("所需藏品不可为空");
      }
    }
  }

  @ApiOperation(value = "ADMIN - " + "合成藏品主体修改", notes = "修改")
  @PostMapping("/couldComUpdate")
  public ApiResponse<Void> couldComUpdate(@RequestBody StoreTreasureUpdateDto dto) {
    if (dto.getNeededTreId() != null && dto.getNeededTreId().size() > 0) {
      if (Strings.isNotEmpty(dto.getTreasureTitle())) {
        if (dto.getUpTime() != null) {
          if (dto.getDownTime() != null) {
            dto.setCouldCompound(1);
            dto.setTType(2);
            ArrayList<String> list = new ArrayList<>();
            list.add(dto.getDetail());
            list.add(dto.getIndexImgPath());
            list.add(dto.getHeadImgPath());
            dto.setHeadImgPath(list.get(0));
            dto.setDetail(list.get(1));
            dto.setIndexImgPath(list.get(2));
            storeTreasureService.update(dto, getAdminUserData());
            return ApiResponse.success();
          } else {
            throw new RuntimeException("开始时间 不可为空");
          }
        } else {
          throw new RuntimeException("结束时间 不可为空");
        }
      } else {
        throw new RuntimeException("藏品标题 不可为空");
      }

    } else {
      throw new RuntimeException("所需藏品不可为空");
    }
  }

  @ApiOperation(value = "ADMIN - " + "合成藏品列表")
  @PostMapping("/couldCompListByPage")
  public ApiResponse<IPage<StoreTreasureDto>> couldCompListByPage(
      @RequestBody PageSelectParam<StoreTreasureSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreTreasure> queryWrapper = new LambdaQueryWrapper<StoreTreasure>();
    ModelMapper md = new ModelMapper();
    md.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    StoreTreasureDto dto = new StoreTreasureDto();
    md.map(param.getSelectParam(), dto);
    queryWrapper.eq(StoreTreasure::getCouldCompound, 1).eq(StoreTreasure::getTType, 2);

    if (param.getSelectParam().getTreasureTitle() != null) {
      queryWrapper.like(StoreTreasure::getTreasureTitle, dto.getTreasureTitle());
    }
    queryWrapper.ne(StoreTreasure::getFromUser, 1);
    ArrayList<Integer> stateList = new ArrayList<>();
    stateList.add(0);
    stateList.add(1);
    stateList.add(2);
    stateList.add(3);
    queryWrapper.in(BaseEntity::getState, stateList);
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    IPage<StoreTreasureDto> page =
        storeTreasureService.getListByPage(
            StoreTreasureDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);
    // headImgPath 是列表的 2b92
    // detail 详情主图限1张 de8.
    // indexImgPath 是作品故事多张
    for (StoreTreasureDto record : page.getRecords()) {
      ArrayList<String> list = new ArrayList<>();
      list.add(record.getDetail());
      list.add(record.getIndexImgPath());
      list.add(record.getHeadImgPath());
      record.setHeadImgPath(list.get(1));
      record.setDetail(list.get(2));
      record.setIndexImgPath(list.get(0));
      record.setLinkInfo(record.getTransationId());
    }

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "查看合成指定藏品所需材料")
  @GetMapping("/couldComp/{id}")
  public ApiResponse<List<StoreTreasureDto>> couldCompById(@PathVariable Long id) {
    // 组合查询条件
    List<StoreTreasureDto> res = storeTreasureService.getCouldCompById(id);
    return ApiResponse.success(res);
  }

  @ApiOperation(value = "ADMIN - " + "添加合成指定藏品所需材料")
  @PostMapping("/addCouldComp")
  public ApiResponse<Object> addCouldComp(@RequestBody AddCouldCompParam param) {
    // 组合查询条件
    String res = storeTreasureService.addCouldComp(param, getAdminUserData());
    return ApiResponse.success(res);
  }

  @ApiOperation(value = "APP - 藏品首发、超前申购 主体列表-tType藏品类型0藏品首发1超前申购")
  //  @AccessLimit(times = 5, second = 10)
  @PostMapping("/appPageList")
  public ApiResponse<IPage<StoreTreasureDto>> appPagelist(
      @RequestBody PageSelectParam<StoreTreasureSelectDto> param) {
    return ApiResponse.success(storeTreasureService.appPagelist(param, getUserData()));
  }

  @ApiOperation(value = "APP - " + "超前申购 中奖名单", notes = "中奖名单")
  @PostMapping("/checkedUserPhone")
  public ApiResponse<IPage<String>> checkedUserPhone(@RequestBody PageSelectParam<Long> param) {
    IPage<String> dto = storeTreasureService.checkedUserPhone(param);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "APP - " + "发售日历", notes = "发售日历")
  @PostMapping("/launchCalendar")
  public ApiResponse<IPage<CalendarResult>> launchCalendar(
      @RequestBody PageSelectParam<Object> param) {
    IPage<CalendarResult> dto = storeTreasureService.launchCalendar(param);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("暂无记录");
    }
  }
}
