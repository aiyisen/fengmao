package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysAddressAddDto;
import com.qy.ntf.bean.dto.SysAddressDto;
import com.qy.ntf.bean.dto.SysAddressUpdateDto;
import com.qy.ntf.bean.entity.SysAddress;
import com.qy.ntf.service.SysAddressService;
import com.qy.ntf.service.SysAreaService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 收货地址 控制层接口
 */
@Api(tags = {"收货地址 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/sysAddress")
public class SysAddressController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 收货地址 服务 */
  @Autowired private SysAddressService sysAddressService;

  @ApiOperation(value = "收货地址详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<SysAddressDto> detail(@PathVariable("id") Long id) {
    SysAddressDto dto = sysAddressService.getSysAddressById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "设置默认收货地址", notes = "根据id查询详情")
  @GetMapping("/updateDefault/{addressId}")
  public ApiResponse<SysAddress> updateDefault(@PathVariable("addressId") Long id) {
    SysAddress dto = sysAddressService.updateDefault(id, getUserData());
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "APP - " + "收货地址添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Void> add(@RequestBody SysAddressAddDto dto) {
    dto.setCreateTime(new Date());
    dto.setCreateId(getUserData().getId());
    dto.setUId(getUserData().getId());
    sysAddressService.save(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "APP - " + "收货地址修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody SysAddressUpdateDto dto) {
    setUpdateUserInfo(dto);
    dto.setUId(getUserData().getId());
    sysAddressService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "APP - " + "收货地址删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    sysAddressService.delete(id);
    return ApiResponse.success();
  }

  @Autowired private SysAreaService sysAreaService;

  @ApiOperation(value = "APP - " + "收货地址分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<SysAddressDto>> listByPage(@RequestBody PageSelectParam<Object> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysAddress> queryWrapper = new LambdaQueryWrapper<SysAddress>();
    queryWrapper.eq(SysAddress::getUId, getUserData().getId());
    queryWrapper.eq(BaseEntity::getState, 1);
    ModelMapper md = new ModelMapper();
    SysAddressDto dto = new SysAddressDto();
    md.map(param.getSelectParam(), dto);
    IPage<SysAddressDto> page =
        sysAddressService.getListByPage(
            SysAddressDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }
}
