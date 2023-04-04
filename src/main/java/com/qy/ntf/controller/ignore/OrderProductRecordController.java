package com.qy.ntf.controller.ignore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.OrderProductRecordAddDto;
import com.qy.ntf.bean.dto.OrderProductRecordDto;
import com.qy.ntf.bean.dto.OrderProductRecordSelectDto;
import com.qy.ntf.bean.dto.OrderProductRecordUpdateDto;
import com.qy.ntf.bean.entity.OrderProductRecord;
import com.qy.ntf.service.OrderProductRecordService;
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
 * @author 王振读 2022-05-25 19:27:16 DESC : 实物商品快照 控制层接口
 */
@Api(tags = {"实物商品快照 api接口"})
@RestController
@ApiIgnore
@RequestMapping("/orderProductRecord")
public class OrderProductRecordController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 实物商品快照 服务 */
  @Autowired private OrderProductRecordService orderProductRecordService;

  @ApiOperation(value = "实物商品快照详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<OrderProductRecordDto> detail(@PathVariable("id") Long id) {
    OrderProductRecordDto dto = orderProductRecordService.getOrderProductRecordById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "实物商品快照添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody OrderProductRecordAddDto dto) {
    setCreateUserInfo(dto);

    orderProductRecordService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "实物商品快照修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody OrderProductRecordUpdateDto dto) {
    setUpdateUserInfo(dto);

    orderProductRecordService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "实物商品快照修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody OrderProductRecordDto tmpDto) {
    ModelMapper md = new ModelMapper();
    OrderProductRecordDto dto = new OrderProductRecordDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    orderProductRecordService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "实物商品快照删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    orderProductRecordService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "实物商品快照分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<OrderProductRecordDto>> listByPage(
      @RequestBody PageSelectParam<OrderProductRecordSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<OrderProductRecord> queryWrapper =
        new LambdaQueryWrapper<OrderProductRecord>();
    ModelMapper md = new ModelMapper();
    OrderProductRecordDto dto = new OrderProductRecordDto();
    md.map(param.getSelectParam(), dto);
    IPage<OrderProductRecordDto> page =
        orderProductRecordService.getListByPage(
            OrderProductRecordDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "实物商品快照列表")
  @PostMapping("/list")
  public ApiResponse<List<OrderProductRecordDto>> listByPage(
      @RequestBody OrderProductRecordDto dto) {
    return ApiResponse.success(orderProductRecordService.list(dto));
  }
}
