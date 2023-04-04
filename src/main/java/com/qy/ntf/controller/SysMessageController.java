package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysMessageAddDto;
import com.qy.ntf.bean.dto.SysMessageDto;
import com.qy.ntf.bean.dto.SysMessageSelectDto;
import com.qy.ntf.bean.dto.SysMessageUpdateDto;
import com.qy.ntf.bean.entity.SysMessage;
import com.qy.ntf.service.SysMessageService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 系统信息 控制层接口
 */
@Api(tags = {"系统信息 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/sysMessage")
public class SysMessageController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 系统信息 服务 */
  @Autowired private SysMessageService sysMessageService;

  @ApiOperation(value = "系统信息详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysMessageDto> detail(@PathVariable("id") Long id) {
    SysMessageDto dto = sysMessageService.getSysMessageById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "ADMIN - " + "系统信息添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysMessageAddDto dto) {
    sysMessageService.save(dto, getAdminUserData());
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统信息修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysMessageUpdateDto dto) {
    setUpdateUserInfo(dto);

    sysMessageService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "系统信息修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody SysMessageDto tmpDto) {
    ModelMapper md = new ModelMapper();
    SysMessageDto dto = new SysMessageDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    sysMessageService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "系统信息删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysMessageService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "系统信息分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysMessageDto>> listByPage(
      @RequestBody PageSelectParam<SysMessageSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysMessage> queryWrapper = new LambdaQueryWrapper<SysMessage>();
    queryWrapper.eq(SysMessage::getMsgType, 0);
    queryWrapper.orderByDesc(SysMessage::getId);
    ModelMapper md = new ModelMapper();
    SysMessageDto dto = new SysMessageDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysMessageDto> page =
        sysMessageService.getListByPage(
            SysMessageDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP - " + "系统信息分页列表")
  @PostMapping("/appPageList")
  public ApiResponse<IPage<SysMessageDto>> appPageList(
      @RequestBody PageSelectParam<SysMessageSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysMessage> queryWrapper = new LambdaQueryWrapper<SysMessage>();
    queryWrapper.eq(SysMessage::getMsgType, param.getSelectParam().getMsgType());
    if (param.getSelectParam().getMsgType() != 0 && getUserData() != null) {
      queryWrapper.eq(BaseEntity::getCreateId, getUserData().getId());
    }

    ModelMapper md = new ModelMapper();
    SysMessageDto dto = new SysMessageDto();
    md.map(param.getSelectParam(), dto);
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    IPage<SysMessageDto> page =
        sysMessageService.getListByPage(
            SysMessageDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);
    for (SysMessageDto record : page.getRecords()) {
      if (record.getMessage().contains("物流单号:")) {
        String[] split = record.getMessage().split("物流单号:");
        record.setLogNum(split[0]);
      }
    }
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "系统信息列表")
  @PostMapping("/list")
  public ApiResponse<List<SysMessageDto>> listByPage(@RequestBody SysMessageDto dto) {
    return ApiResponse.success(sysMessageService.list(dto));
  }
}
