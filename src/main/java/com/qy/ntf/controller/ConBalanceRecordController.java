package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.ConBalanceRecordAddDto;
import com.qy.ntf.bean.dto.ConBalanceRecordDto;
import com.qy.ntf.bean.dto.ConBalanceRecordSelectDto;
import com.qy.ntf.bean.dto.ConBalanceRecordUpdateDto;
import com.qy.ntf.bean.entity.ConBalanceRecord;
import com.qy.ntf.service.ConBalanceRecordService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 余额记录 控制层接口
 */
@Api(tags = {"余额记录 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/conBalanceRecord")
public class ConBalanceRecordController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 余额记录 服务 */
  @Autowired private ConBalanceRecordService conBalanceRecordService;

  @ApiOperation(value = "余额记录详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<ConBalanceRecordDto> detail(@PathVariable("id") Long id) {
    ConBalanceRecordDto dto = conBalanceRecordService.getConBalanceRecordById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "余额记录添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody ConBalanceRecordAddDto dto) {
    setCreateUserInfo(dto);

    conBalanceRecordService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "余额记录修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody ConBalanceRecordUpdateDto dto) {
    setUpdateUserInfo(dto);

    conBalanceRecordService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "余额记录修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody ConBalanceRecordDto tmpDto) {
    ModelMapper md = new ModelMapper();
    ConBalanceRecordDto dto = new ConBalanceRecordDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    conBalanceRecordService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "余额记录删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    conBalanceRecordService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "APP - " + "余额记录分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<ConBalanceRecordDto>> listByPage(
      @RequestBody PageSelectParam<ConBalanceRecordSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<ConBalanceRecord> queryWrapper = new LambdaQueryWrapper<ConBalanceRecord>();
    queryWrapper.eq(ConBalanceRecord::getCreateId, getUserData().getId());
    ModelMapper md = new ModelMapper();
    ConBalanceRecordDto dto = new ConBalanceRecordDto();
    md.map(param.getSelectParam(), dto);
    queryWrapper.orderByDesc(BaseEntity::getCreateTime);
    IPage<ConBalanceRecordDto> page =
        conBalanceRecordService.getListByPage(
            ConBalanceRecordDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "余额记录列表")
  @PostMapping("/list")
  public ApiResponse<List<ConBalanceRecordDto>> listByPage(@RequestBody ConBalanceRecordDto dto) {
    return ApiResponse.success(conBalanceRecordService.list(dto));
  }
}
