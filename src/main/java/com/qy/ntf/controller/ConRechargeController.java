package com.qy.ntf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.ConRecharge;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.service.ConRechargeService;
import com.qy.ntf.service.SysUserService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import com.qy.ntf.util.wxPay.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王振读 2022-05-25 19:27:16 DESC : 充值订单表 控制层接口
 */
@Api(tags = {"充值订单表 api接口"})
@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/conRecharge")
public class ConRechargeController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 充值订单表 服务 */
  @Autowired private ConRechargeService conRechargeService;

  @Autowired private SysUserService sysUserService;

  @ApiOperation(value = "充值订单表详情", notes = "根据id查询详情")
  @GetMapping("/detail/{id}")
  public ApiResponse<ConRechargeDto> detail(@PathVariable("id") Long id) {
    ConRechargeDto dto = conRechargeService.getConRechargeById(id);
    if (null != dto) {
      return ApiResponse.success(dto);
    } else {
      return ApiResponse.fail("查询信息不存在");
    }
  }

  @ApiOperation(value = "APP - " + "充值订单表添加", notes = "添加")
  @PostMapping("/add")
  public ApiResponse<Object> add(@RequestBody ConRechargeAddDto dto, HttpServletRequest request) {
    if (dto.getPayType() == 2) {
      UserDto userData = getUserData();
      SysUser sysUser = sysUserService.selectDataById(userData.getId());
      if (sysUser.getIsOpening() != 1) {
        throw new RuntimeException("对不起用户暂未完成开户");
      }
    }
    return ApiResponse.success(
        conRechargeService.save(dto, getUserData(), WXPayUtil.getIpAddress(request)));
  }

  @ApiOperation(value = "充值订单表修改", notes = "修改")
  @PostMapping("/update")
  public ApiResponse<Void> update(@RequestBody ConRechargeUpdateDto dto) {
    setUpdateUserInfo(dto);

    conRechargeService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "充值订单表修改状态", notes = "修改状态")
  @PostMapping("/update/state")
  public ApiResponse<Void> updateState(@RequestBody ConRechargeDto tmpDto) {
    ModelMapper md = new ModelMapper();
    ConRechargeDto dto = new ConRechargeDto();
    md.map(tmpDto, dto);
    setUpdateUserInfo(dto);

    conRechargeService.updateState(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "充值订单表删除", notes = "根据id删除")
  @DeleteMapping("/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    conRechargeService.delete(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "充值订单表分页列表")
  @PostMapping("/page")
  public ApiResponse<IPage<ConRechargeDto>> listByPage(
      @RequestBody PageSelectParam<ConRechargeSelectDto> param) {
    // 组合查询条件
    LambdaQueryWrapper<ConRecharge> queryWrapper = new LambdaQueryWrapper<ConRecharge>();
    queryWrapper.eq(ConRecharge::getUId, getUserData().getId());
    ModelMapper md = new ModelMapper();
    ConRechargeDto dto = new ConRechargeDto();
    md.map(param.getSelectParam(), dto);
    IPage<ConRechargeDto> page =
        conRechargeService.getListByPage(
            ConRechargeDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper);

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "APP - " + "充值订单表列表")
  @PostMapping("/list")
  public ApiResponse<List<ConRechargeDto>> listByPage(@RequestBody ConRechargeDto dto) {
    return ApiResponse.success(conRechargeService.list(dto, getUserData()));
  }
}
