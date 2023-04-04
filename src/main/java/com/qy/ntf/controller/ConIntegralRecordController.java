package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.customResult.InviteRecordRes;
import com.qy.ntf.bean.dto.ConIntegralRecordAddDto;
import com.qy.ntf.bean.dto.ConIntegralRecordDto;
import com.qy.ntf.bean.dto.ConIntegralRecordUpdateDto;
import com.qy.ntf.bean.entity.ConIntegralRecord;
import com.qy.ntf.service.ConIntegralRecordService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 积分流水记录（记录总和>=0) 控制层接口
 */
@Api(tags = {"积分流水记录（记录总和>=0) api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/conIntegralRecord")
public class ConIntegralRecordController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 积分流水记录（记录总和>=0) 服务 */
  @Autowired private ConIntegralRecordService conIntegralRecordService;

  @ApiOperation(value = "积分流水记录（记录总和>=0)详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<ConIntegralRecordDto> detail(@PathVariable("id") Long id) {
    ConIntegralRecordDto dto = conIntegralRecordService.getConIntegralRecordById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "APP - " + "获取邀请用户列表", notes = "邀请好友列表")
  @GetMapping("/getInviteRecord")
  public ApiResponse<List<InviteRecordRes>> getInviteRecord() {
    return ApiResponse.success(conIntegralRecordService.getInviteRecord(getUserData()));
  }

  @ApiOperation(value = "积分流水记录（记录总和>=0)添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody ConIntegralRecordAddDto dto) {
    setCreateUserInfo(dto);

    conIntegralRecordService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "积分流水记录（记录总和>=0)修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody ConIntegralRecordUpdateDto dto) {
    setUpdateUserInfo(dto);

    conIntegralRecordService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "积分流水记录（记录总和>=0)修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody ConIntegralRecordDto tmpDto) {
    ModelMapper md = new ModelMapper();
    ConIntegralRecordDto dto = new ConIntegralRecordDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    conIntegralRecordService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "积分流水记录（记录总和>=0)删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    conIntegralRecordService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "APP - " + "积分流水记录（记录总和>=0)分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<ConIntegralRecordDto>> listByPage(
      @RequestBody PageSelectParam<Object> param) {
    // 组合查询条件
    LambdaQueryWrapper<ConIntegralRecord> queryWrapper =
        new LambdaQueryWrapper<ConIntegralRecord>();
    queryWrapper.eq(ConIntegralRecord::getUId, getUserData().getId());
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    IPage<ConIntegralRecordDto> page =
        conIntegralRecordService.getListByPage(
            ConIntegralRecordDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);
    //    for (ConIntegralRecordDto record : page.getRecords()) {
    //      // 积分记录类型0登录赠送1邀请好友赠送2订单消费3刷一刷
    //      if (record.getRecordType() == 2) {
    //        record.setMetaCount(record.getMetaCount() * -1);
    //      }
    //    }
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "积分流水记录（记录总和>=0)列表")
  @PostMapping("/list")
  public ApiResponse<List<ConIntegralRecordDto>> listByPage(@RequestBody ConIntegralRecordDto dto) {
    return ApiResponse.success(conIntegralRecordService.list(dto));
  }

  @ApiOperation(value = "APP - " + "查询用户是否领取登录积分")
  @PostMapping("/todayGet")
  public ApiResponse<Object> todayGet() {
    return ApiResponse.success(conIntegralRecordService.todayGet(getUserData()));
  }

  @ApiOperation(value = "APP - " + "查询用户是否领取登录积分")
  @PostMapping("/getTodayIntegral")
  public ApiResponse<Object> getTodayIntegral() {
    return ApiResponse.success(conIntegralRecordService.getTodayIntegral(getUserData()));
  }
}
