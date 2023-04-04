package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.OrderProPoolRecordAddDto;
import com.qy.ntf.bean.dto.OrderProPoolRecordDto;
import com.qy.ntf.bean.dto.OrderProPoolRecordSelectDto;
import com.qy.ntf.bean.dto.OrderProPoolRecordUpdateDto;
import com.qy.ntf.bean.entity.OrderProPoolRecord;
import com.qy.ntf.service.OrderProPoolRecordService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 聚合池商品快照 控制层接口
 */
@Api(tags = {"聚合池商品快照 api接口"})
@RestController
@ApiIgnore
@RequestMapping("/orderProPoolRecord")
public class OrderProPoolRecordController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 聚合池商品快照 服务 */
  @Autowired private OrderProPoolRecordService orderProPoolRecordService;

  @ApiOperation(value = "聚合池商品快照详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<OrderProPoolRecordDto> detail(@PathVariable("id") Long id) {
    OrderProPoolRecordDto dto = orderProPoolRecordService.getOrderProPoolRecordById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "聚合池商品快照添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody OrderProPoolRecordAddDto dto) {
    setCreateUserInfo(dto);

    orderProPoolRecordService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "聚合池商品快照修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody OrderProPoolRecordUpdateDto dto) {
    setUpdateUserInfo(dto);

    orderProPoolRecordService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "聚合池商品快照修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody OrderProPoolRecordDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderProPoolRecordDto dto = new OrderProPoolRecordDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    orderProPoolRecordService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "聚合池商品快照删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    orderProPoolRecordService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "聚合池商品快照分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<OrderProPoolRecordDto>> listByPage(
      @RequestBody PageSelectParam<OrderProPoolRecordSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<OrderProPoolRecord> queryWrapper =
        new LambdaQueryWrapper<OrderProPoolRecord>();
    ModelMapper md = new ModelMapper();
    OrderProPoolRecordDto dto = new OrderProPoolRecordDto();
    md.map(param.getSelectParam(), dto);
    IPage<OrderProPoolRecordDto> page =
        orderProPoolRecordService.getListByPage(
            OrderProPoolRecordDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "聚合池商品快照列表")
  @PostMapping("/list")
  public ApiResponse<List<OrderProPoolRecordDto>> listByPage(
      @RequestBody OrderProPoolRecordDto dto) {
    return ApiResponse.success(orderProPoolRecordService.list(dto));
  }
}
