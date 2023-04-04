package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.StoreTreIosPriceAddDto;
import com.qy.ntf.bean.dto.StoreTreIosPriceDto;
import com.qy.ntf.bean.dto.StoreTreIosPriceSelectDto;
import com.qy.ntf.bean.dto.StoreTreIosPriceUpdateDto;
import com.qy.ntf.bean.entity.StoreTreIosPrice;
import com.qy.ntf.service.StoreTreIosPriceService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 王振读 2022-07-31 20:17:27 DESC : ios价格 控制层接口
 */
@Api(tags = {"ios价格 api接口"})
@RestController
@Slf4j
@RequestMapping("/storeTreIosPrice")
public class StoreTreIosPriceController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** ios价格 服务 */
  @Autowired private StoreTreIosPriceService storeTreIosPriceService;

  @ApiOperation(value = "ADMIN - " + "ios价格添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(
      @RequestBody @Valid StoreTreIosPriceAddDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      setCreateUserInfo(dto);
      storeTreIosPriceService.save(dto);
      return ApiResponse.success();
    }
  }

  @ApiOperation(value = "ADMIN - " + "ios价格修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(
      @RequestBody @Valid StoreTreIosPriceUpdateDto dto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ApiResponse.fail(
          bindingResult.getFieldErrors().stream()
              .map(o -> o.getField() + o.getDefaultMessage())
              .collect(Collectors.joining(",")));
    } else {
      setUpdateUserInfo(dto);

      storeTreIosPriceService.update(dto);
      return ApiResponse.success();
    }
  }

  @ApiOperation(value = "ADMIN - " + "ios价格删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    storeTreIosPriceService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "ios价格分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<StoreTreIosPriceDto>> listByPage(
      @RequestBody PageSelectParam<StoreTreIosPriceSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<StoreTreIosPrice> queryWrapper = new LambdaQueryWrapper<StoreTreIosPrice>();
    ModelMapper md = new ModelMapper();
    StoreTreIosPriceDto dto = new StoreTreIosPriceDto();
    md.map(param.getSelectParam(), dto);
    queryWrapper.eq(BaseEntity::getState, 1);
    IPage<StoreTreIosPriceDto> page =
        storeTreIosPriceService.getListByPage(
            StoreTreIosPriceDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "ios价格列表-不分页")
  @PostMapping("/list")
  public ApiResponse<List<StoreTreIosPriceDto>> listByPage(@RequestBody StoreTreIosPriceDto dto) {
    return ApiResponse.success(storeTreIosPriceService.list(dto));
  }
}
