package com.qy.ntf.controller;

import com.qy.ntf.base.BaseController;
import com.qy.ntf.bean.customResult.CellTotal;
import com.qy.ntf.bean.customResult.IndexData;
import com.qy.ntf.service.OrderProductService;
import com.qy.ntf.service.OrderTreasurePoolService;
import com.qy.ntf.service.SysCollectService;
import com.qy.ntf.service.SysUserService;
import com.qy.ntf.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.controller @ClassName:
 * SystemController @Author: 王振读 @Description: ${description} @Date: 2021/11/26 9:31 @Version: 1.0
 */
@RestController
@RequestMapping("/collect")
@Api(tags = {"ADMIN-系统管理"})
public class CollectController extends BaseController {
  @Autowired private SysUserService userService;
  @Autowired private OrderProductService orderProductService;
  @Autowired private OrderTreasurePoolService orderTreasurePoolService;

  @ApiOperation(value = "ADMIN - " + "注册用户统计信息")
  @PostMapping("/userCollect")
  public ApiResponse<CellTotal> userCollect() {
    // 组合查询条件
    CellTotal page = userService.getCellTotal();
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "权益商城统计信息")
  @PostMapping("/productCollect")
  public ApiResponse<CellTotal> productCollect() {
    // 组合查询条件
    CellTotal page = orderProductService.productCollect();
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "超前申购统计信息")
  @PostMapping("/beforeCollect")
  public ApiResponse<CellTotal> beforeCollect() {
    // 组合查询条件
    CellTotal page = orderTreasurePoolService.beforeCollect();

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "藏品统计信息")
  @PostMapping("/treasureCollect")
  public ApiResponse<CellTotal> treasureCollect() {
    // 组合查询条件
    CellTotal page = orderTreasurePoolService.treasureCollect();
    return ApiResponse.success(page);
  }

  @Autowired private SysCollectService sysCollectService;

  @ApiOperation(value = "ADMIN - " + "访问量统计信息")
  @PostMapping("/findCollect")
  public ApiResponse<CellTotal> findCollect() {
    // 组合查询条件
    CellTotal page = sysCollectService.treasureCollect();
    return ApiResponse.success(page);
  }

  @ApiOperation(value = "ADMIN - " + "首页，流量，每日总销售额")
  @PostMapping("/indexData")
  public ApiResponse<IndexData> indexData() {
    // 组合查询条件
    IndexData page = sysCollectService.indexData();
    return ApiResponse.success(page);
  }
}
