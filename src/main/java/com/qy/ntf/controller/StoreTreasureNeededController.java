package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.customResult.NeededRes;
import com.qy.ntf.bean.customResult.TreaTrading;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.StoreTreasureNeeded;
import com.qy.ntf.service.StoreTreasureNeededService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 王振读 2022-05-28 15:09:30 DESC : 控制层接口
 */
@Api(tags = {" api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/storeTreasureNeeded")
@Slf4j
public class StoreTreasureNeededController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 服务 */
  @Autowired private StoreTreasureNeededService storeTreasureNeededService;

  @ApiOperation(value = "详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<StoreTreasureNeededDto> detail(@PathVariable("id") Long id) {
    StoreTreasureNeededDto dto = storeTreasureNeededService.getStoreTreasureNeededById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody StoreTreasureNeededAddDto dto) {
    setCreateUserInfo(dto);

    storeTreasureNeededService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody StoreTreasureNeededUpdateDto dto) {
    setUpdateUserInfo(dto);

    storeTreasureNeededService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody StoreTreasureNeededDto tmpDto) {
    ModelMapper md = new ModelMapper();
    StoreTreasureNeededDto dto = new StoreTreasureNeededDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    storeTreasureNeededService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    storeTreasureNeededService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<StoreTreasureNeededDto>> listByPage(
      @RequestBody PageSelectParam<StoreTreasureNeededSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreTreasureNeeded> queryWrapper =
        new LambdaQueryWrapper<StoreTreasureNeeded>();

    ModelMapper md = new ModelMapper();
    StoreTreasureNeededDto dto = new StoreTreasureNeededDto();
    md.map(param.getSelectParam(), dto);
    IPage<StoreTreasureNeededDto> page =
        storeTreasureNeededService.getListByPage(
            StoreTreasureNeededDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "交易明细列表--2收入-1支出1赠送2获赠3出售4购买")
  @PostMapping("/tradingPage")
  public ApiResponse<IPage<TreaTrading>> tradingPage(@RequestBody PageSelectParam<Integer> param) {
    return ApiResponse.success(storeTreasureNeededService.tradingPage(param, getUserData()));
  }

  @ApiOperation(value = "APP - " + "获取允许合成藏品列表列表")
  @PostMapping("/getColudNeededList")
  public ApiResponse<List<StoreTreasureDto>> getColudNeededList() {
    return ApiResponse.success(storeTreasureNeededService.getColudNeededList());
  }

  @ApiOperation(value = "APP - " + "获取合成藏品所需材料列表列表")
  @PostMapping("/getNeededList/{id}")
  public ApiResponse<List<NeededRes>> getNeededList(@PathVariable("id") Long id) {
    return ApiResponse.success(storeTreasureNeededService.getNeededList(id, getUserData()));
  }

  @Autowired private RedissonClient redissonClient;

  @ApiOperation(value = "APP - " + "合成指定藏品")
  @PostMapping("/addCompound/{id}")
  public ApiResponse<String> addCompound(@PathVariable("id") Long id) {

    RLock rLock = redissonClient.getLock("LOCK_Comp_" + id);
    UserDto userData = getUserData();
    try {
      if (rLock.tryLock(0, 30, TimeUnit.SECONDS)) {
        ApiResponse<String> success =
            ApiResponse.success(storeTreasureNeededService.addCompound(id, getUserData()));
        if (rLock.isLocked()) {
          if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
            rLock.unlock(); // 释放锁
          }
        }
        return success;
      }
    } catch (Exception e) {
      if (rLock.isLocked()) {
        if (rLock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
          rLock.unlock(); // 释放锁
        }
      }
      return ApiResponse.fail(e.getMessage());
    }
    log.info("合成藏品请求结束：treId:" + id + " userId: " + userData.getId());
    return ApiResponse.fail("服务器爆满请稍后再试");
  }
}
