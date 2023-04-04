package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.customResult.MyTreasure;
import com.qy.ntf.bean.customResult.StorePoolPriceRecord;
import com.qy.ntf.bean.dto.StoreProPoolDto;
import com.qy.ntf.bean.dto.StoreTreasureDto;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.GivingParam;
import com.qy.ntf.service.StoreProPoolService;
import com.qy.ntf.service.SysDictonaryService;
import com.qy.ntf.service.SysOrgService;
import com.qy.ntf.service.SysUserService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池主体 控制层接口
 */
@Api(tags = {"聚合池主体 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/storeProPool")
public class StoreProPoolController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 聚合池主体 服务 */
  @Autowired private StoreProPoolService storeProPoolService;

  //  @ApiOperation(value = "聚合池主体详情", notes = "根据id查询详情")
  //  @GetMapping("/detail/{id}")
  //  public ApiResponse<StoreProPoolDto> detail(@PathVariable("id") Long id) {
  //    StoreProPoolDto dto = storeProPoolService.getStoreProPoolById(id);
  //    if (null != dto) {
  //      return ApiResponse.success(dto);
  //    } else {
  //      return ApiResponse.fail("查询信息不存在");
  //    }
  //  }
  //
  //  @ApiOperation(value = "ADMIN - " + "聚合池主体添加", notes = "添加")
  //  @PostMapping("/add")
  //  public ApiResponse<Void> add(@RequestBody StoreProPoolAddDto dto) {
  //    setCreateUserInfo(dto);
  //    storeProPoolService.save(dto);
  //    return ApiResponse.success();
  //  }
  //
  //  @ApiOperation(value = "ADMIN - " + "聚合池主体修改", notes = "修改")
  //  @PostMapping("/update")
  //  public ApiResponse<Void> update(@RequestBody StoreProPoolUpdateDto dto) {
  //    setUpdateUserInfo(dto);
  //    storeProPoolService.update(dto);
  //    return ApiResponse.success();
  //  }
  //
  //  @ApiOperation(value = "ADMIN - " + "聚合池主体修改状态", notes = "修改状态")
  //  @PostMapping("/update/state")
  //  public ApiResponse<Void> updateState(@RequestBody StoreProPoolDto tmpDto) {
  //    ModelMapper md = new ModelMapper();
  //    StoreProPoolDto dto = new StoreProPoolDto();
  //    md.map(tmpDto, dto);
  //    setUpdateUserInfo(dto);
  //
  //    storeProPoolService.updateState(dto);
  //    return ApiResponse.success();
  //  }
  //
  //  @ApiOperation(value = "ADMIN - " + "聚合池主体删除", notes = "根据id删除")
  //  @DeleteMapping("/delete/{id}")
  //  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
  //    storeProPoolService.delete(id, getAdminUserData());
  //    return ApiResponse.success();
  //  }

  @Autowired private SysOrgService sysOrgService;

  //  @ApiOperation(value = "ADMIN - " + "聚合池主体分页列表")
  //  @PostMapping("/page")
  //  public ApiResponse<IPage<StoreProPoolDto>> listByPage(
  //      @RequestBody PageSelectParam<Object> param) {
  //    // 组合查询条件
  //    LambdaQueryWrapper<StoreProPool> queryWrapper = new LambdaQueryWrapper<StoreProPool>();
  //    queryWrapper.ne(BaseEntity::getState, -1);
  //    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
  //    ModelMapper md = new ModelMapper();
  //    StoreProPoolDto dto = new StoreProPoolDto();
  //    md.map(param.getSelectParam(), dto);
  //    IPage<StoreProPoolDto> page =
  //        storeProPoolService.getListByPage(
  //            StoreProPoolDto.class,
  //            param.getPageNum(),
  //            Long.parseLong(param.getPageSize().toString()),
  //            queryWrapper);
  //    List<SysUser> sysUsers =
  //        sysUserService.getUserByIds(
  //            page.getRecords().stream()
  //                .filter(o -> o.getFromType() != null && o.getFromType() == 1)
  //                .map(StoreProPoolDto::getCreateId)
  //                .collect(Collectors.toList()));
  //    Map<Long, SysUser> collect =
  //        sysUsers.stream().collect(Collectors.toMap(SysUser::getId, o -> o));
  //    for (StoreProPoolDto t : page.getRecords()) {
  //      t.setCreater(collect.get(t.getCreateId()));
  //    }
  //    List<Long> orgIds =
  //        page.getRecords().stream()
  //            .filter(o -> o.getFromType() != null && o.getFromType() == 0)
  //            .map(StoreProPoolDto::getSysOrgId)
  //            .collect(Collectors.toList());
  //    if (orgIds.size() > 0) {
  //      Map<Long, SysOrg> sysOrgs =
  //          sysOrgService.getListByIds(orgIds).stream()
  //              .collect(Collectors.toMap(SysOrg::getId, t -> t));
  //      page.getRecords()
  //          .forEach(
  //              o -> {
  //                if (o.getFromType() != null && o.getFromType() == 0 && o.getSysOrgId() != null)
  // {
  //                  o.setSysOrg(sysOrgs.get(o.getSysOrgId()));
  //                }
  //              });
  //    }
  //    return ApiResponse.success(page);
  //  }

  //  @ApiOperation(value = "聚合池主体列表")
  //  @PostMapping("/list")
  //  public ApiResponse<List<StoreProPoolDto>> listByPage(@RequestBody StoreProPoolDto dto) {
  //    return ApiResponse.success(storeProPoolService.list(dto));
  //  }

  @Autowired private SysDictonaryService sysDictonaryService;

  //  @ApiOperation(value = "APP - " + "聚合池列表")
  //  @PostMapping("/appPageList")
  //  public CustomResponse<IPage<StoreProPoolDto>> appPagelist(
  //      @RequestBody PageSelectParam<StoreProPoolDto> param) {
  //    String percent = sysDictonaryService.getDicByAlias("pool_send_percent");
  //    String maxMeta = sysDictonaryService.getDicByAlias("pool_send_max_meta");
  //    CustomResponse<IPage<StoreProPoolDto>> iPageCustomResponse =
  //        storeProPoolService.appPagelist(param, getUserData(), percent, maxMeta);
  //    List<Long> orgIds =
  //        iPageCustomResponse.getData().getRecords().stream()
  //            .filter(o -> o.getFromType() != null && o.getFromType() == 0)
  //            .map(StoreProPoolDto::getSysOrgId)
  //            .collect(Collectors.toList());
  //    if (orgIds.size() > 0) {
  //      Map<Long, SysOrg> sysOrgs =
  //          sysOrgService.getListByIds(orgIds).stream()
  //              .collect(Collectors.toMap(SysOrg::getId, t -> t));
  //      iPageCustomResponse
  //          .getData()
  //          .getRecords()
  //          .forEach(
  //              o -> {
  //                if (o.getFromType() != null && o.getFromType() == 0 && o.getSysOrgId() != null)
  // {
  //                  o.setSysOrg(sysOrgs.get(o.getSysOrgId()));
  //                }
  //              });
  //    }
  //    return iPageCustomResponse;
  //  }

  @ApiOperation(value = "APP - " + "查询全部历史价格用户详情列表页", notes = "查询全部历史价格用户详情列表页")
  @PostMapping("/getPoolPriceRecordByPage")
  public ApiResponse<IPage<StorePoolPriceRecord>> getPoolPriceRecordByPage(
      @RequestBody PageSelectParam<StoreProPoolDto> param) {
    return ApiResponse.success(storeProPoolService.getPoolPriceRecordByPage(param, -1));
  }

  @ApiOperation(
      value = "APP - " + "查询全部历史价格图表数据type：0一周1一月2三月3一年",
      notes = "查询全部历史价格图表数据type：0一周1一月2三月3一年")
  @GetMapping("/getPoolPriceRecord/{id}/{type}")
  public ApiResponse<List<StorePoolPriceRecord>> getPoolPriceRecord(
      @PathVariable("id") Long id, @PathVariable("type") Integer type) {
    return ApiResponse.success(storeProPoolService.getPoolPriceRecord(id, type));
  }

  @ApiOperation(value = "APP - " + "获取我的藏品", notes = "获取我的藏品")
  @PostMapping("/myTreasure")
  public ApiResponse<List<MyTreasure>> getMytreasure(@RequestBody MyTreasure param) {
    return ApiResponse.success(storeProPoolService.getMytreasure(param, getUserData()));
  }

  @ApiOperation(value = "ADMIN - " + "转增藏品分页列表", notes = "转增藏品分页列表")
  @PostMapping("/turnTreasure")
  public ApiResponse<IPage<StoreTreasureDto>> turnTreasure(
      @RequestBody PageSelectParam<String> param) {
    return ApiResponse.success(storeProPoolService.turnTreasure(param, getUserData()));
  }

  @ApiOperation(value = "ADMIN - " + "寄售藏品分页列表", notes = "寄售藏品分页列表")
  @PostMapping("/isSalingTreasure")
  public ApiResponse<IPage<StoreTreasureDto>> isSalingTreasure(
      @RequestBody PageSelectParam<String> param) {
    return ApiResponse.success(storeProPoolService.isSalingTreasure(param, getUserData()));
  }

  @ApiOperation(value = "APP - " + "开盲盒", notes = "获取我的藏品")
  @PostMapping("/openBindBox")
  public ApiResponse<MyTreasure> openBindBox(@RequestBody MyTreasure param) {
    return ApiResponse.success(storeProPoolService.openBindBox(param, getUserData()));
  }

  @ApiOperation(value = "APP - " + "取消出售藏品", notes = "取消出售藏品")
  @PostMapping("/cancelOut")
  public ApiResponse<Object> cancelOut(@RequestBody MyTreasure myTreasure) {
    return ApiResponse.success(storeProPoolService.cancelOut(myTreasure, getUserData()));
  }

  @ApiOperation(value = "APP - " + "修改藏品价格及标签", notes = "修改藏品价格及标签")
  @PostMapping("/updatePoolPriceAndTag")
  public ApiResponse<Object> updatePoolPriceAndTag(@RequestBody MyTreasure myTreasure) {
    return ApiResponse.success(
        storeProPoolService.updatePoolPriceAndTag(myTreasure, getUserData()));
  }

  @Autowired private SysUserService sysUserService;

  @ApiOperation(value = "APP - " + "根据邀请码查询用户", notes = "获取用户")
  @GetMapping("/users/{userIndex}")
  public ApiResponse<List<SysUser>> getUserByIndex(@PathVariable("userIndex") String userIndex) {
    List<SysUser> user = sysUserService.getUserByIndex(userIndex);
    return ApiResponse.success(user);
  }

  @ApiOperation(value = "APP - " + "赠送藏品给指定用户/出售藏品", notes = "赠送")
  @PostMapping("/users/givingTreasure")
  public ApiResponse<Object> givingTreasure(@RequestBody GivingParam param) {
    return ApiResponse.success(sysUserService.givingTreasure(param, getUserData()));
  }

  @ApiOperation(value = "APP - " + "聚合池刷一刷领取积分", notes = "聚合池刷一刷领取积分")
  @GetMapping("/reFresh/{count}")
  public ApiResponse<String> reFresh(@PathVariable("count") Integer count) {
    return ApiResponse.success(storeProPoolService.reFresh(getUserData(), count));
  }
}
