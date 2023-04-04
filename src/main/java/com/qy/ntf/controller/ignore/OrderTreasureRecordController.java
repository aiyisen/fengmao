package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.OrderTreasureRecordAddDto;
import com.qy.ntf.bean.dto.OrderTreasureRecordDto;
import com.qy.ntf.bean.dto.OrderTreasureRecordSelectDto;
import com.qy.ntf.bean.dto.OrderTreasureRecordUpdateDto;
import com.qy.ntf.bean.entity.OrderTreasureRecord;
import com.qy.ntf.service.OrderTreasureRecordService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 藏品快照 控制层接口
 */
@Api(tags = {"藏品快照 api接口"})
@RestController
@ApiIgnore
@RequestMapping("/orderTreasureRecord")
public class OrderTreasureRecordController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 藏品快照 服务 */
  @Autowired private OrderTreasureRecordService orderTreasureRecordService;

  @ApiOperation(value = "藏品快照详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<OrderTreasureRecordDto> detail(@PathVariable("id") Long id) {
    OrderTreasureRecordDto dto = orderTreasureRecordService.getOrderTreasureRecordById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "藏品快照添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody OrderTreasureRecordAddDto dto) {
    setCreateUserInfo(dto);

    orderTreasureRecordService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "藏品快照修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody OrderTreasureRecordUpdateDto dto) {
    setUpdateUserInfo(dto);

    orderTreasureRecordService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "藏品快照修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody OrderTreasureRecordDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderTreasureRecordDto dto = new OrderTreasureRecordDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    orderTreasureRecordService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "藏品快照删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    orderTreasureRecordService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "藏品快照分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<OrderTreasureRecordDto>> listByPage(
      @RequestBody PageSelectParam<OrderTreasureRecordSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<OrderTreasureRecord> queryWrapper =
        new LambdaQueryWrapper<OrderTreasureRecord>();
    ModelMapper md = new ModelMapper();
    OrderTreasureRecordDto dto = new OrderTreasureRecordDto();
    md.map(param.getSelectParam(), dto);
    IPage<OrderTreasureRecordDto> page =
        orderTreasureRecordService.getListByPage(
            OrderTreasureRecordDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "藏品快照列表")
  @PostMapping("/list")
  public ApiResponse<List<OrderTreasureRecordDto>> listByPage(
      @RequestBody OrderTreasureRecordDto dto) {
    return ApiResponse.success(orderTreasureRecordService.list(dto));
  }
}
