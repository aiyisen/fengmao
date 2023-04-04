package com.qy.ntf.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.SysDropDto;
import com.qy.ntf.bean.entity.SysDrop;
import com.qy.ntf.bean.entity.SysUser;
import com.qy.ntf.bean.param.AddDropParam;
import com.qy.ntf.bean.param.TopSelectParam;
import com.qy.ntf.config.AccessLimit;
import com.qy.ntf.service.SysDropService;
import com.qy.ntf.service.SysUserService;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.PageSelectParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 王振读 2022-08-15 15:18:56 DESC : 控制层接口
 */
@Api(tags = {" 空投api接口"})
@RestController
@Slf4j
@RequestMapping("/sysDrop")
public class SysDropController extends BaseController {

  /** 请求 */
  @Autowired protected HttpServletRequest request;

  /** 服务 */
  @Autowired private SysDropService sysDropService;

  @ApiOperation(value = "空投分页列表")
  @PostMapping("/page") // selectParam0 普通藏品1盲盒
  public ApiResponse<IPage<SysDropDto>> listByPage(@RequestBody PageSelectParam<Integer> param) {
    // 组合查询条件
    LambdaQueryWrapper<SysDrop> queryWrapper = new LambdaQueryWrapper<SysDrop>();
    IPage<SysDropDto> page =
        sysDropService.getListByPage(
            SysDropDto.class,
            param.getPageNum(),
            Long.parseLong(param.getPageSize().toString()),
            queryWrapper,
            param.getSelectParam());

    return ApiResponse.success(page);
  }

  @ApiOperation(value = "发起空投")
  @PostMapping("/addDrop")
  public ApiResponse<String> addDrop(@RequestBody AddDropParam param) {
    if (param.getId() == null && param.getIntegral() == null) {
      throw new RuntimeException("赠送积分与藏品不可同时为空");
    }
    if (param.getId() == null) {
      if (param.getIntegral() <= 0) {
        throw new RuntimeException("赠送积分值不合法");
      }
    }
    sysDropService.addDrop(param, getAdminUserData());

    return ApiResponse.success("success");
  }
  // 导入空投
  @AccessLimit(times = 1, second = 5)
  @PostMapping("/dropByImport/{streId}")
  public ApiResponse<String> importTest(
      @RequestParam("file") MultipartFile file, @PathVariable("streId") Long streId) {
    // 判断上传文件为空返回
    if (file.isEmpty()) {
      return ApiResponse.fail("上传文件不可为空");
    }
    // 判断文件是否是excel文件
    String filename = file.getOriginalFilename();
    if (!StringUtils.isEmpty(filename)
        && (!filename.matches("^.+\\.(?i)(xls)$") && !filename.matches("^.+\\.(?i)(xlsx)$"))) {
      return ApiResponse.fail("上传文件格式错误，请上传后缀为.xls或.xlsx的文件");
    }
    // 读取文件
    ExcelReader readerExcel = null;
    try {
      readerExcel = ExcelUtil.getReader(file.getInputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }

    int total = readerExcel.getRowCount();
    // 存读取到的内容
    if (total > 1) {
      Set<Map<String, Object>> mobiles = new HashSet<>();
      List<List<Object>> read = readerExcel.read(1);

      if (read != null && read.size() > 0) {
        for (List<Object> cell : read) {
          if (cell != null
              && cell.size() > 0
              && cell.get(0) != null
              && Strings.isNotEmpty(cell.get(0).toString())) {
            HashMap<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("mobile", cell.get(0).toString());
            tmpMap.put("count", cell.get(1).toString());
            mobiles.add(tmpMap);
          }
        }
        String s = sysDropService.addImportDrop(mobiles, streId, getAdminUserData());
        return ApiResponse.success(s);
      }
      return ApiResponse.success();
    } else {
      return ApiResponse.fail("导入表格数据为空");
    }
  }

  @ApiOperation(value = "根据持有藏品查询用户列表-无分页")
  @PostMapping("/findUserByTreaId")
  public ApiResponse<List<SysUser>> findUserByTreaId(@RequestBody List<Long> ids) {
    if (ids != null && ids.size() > 0) {
      List<SysUser> sysUsers = sysUserService.findUserByTreaId(ids);
      return ApiResponse.success(sysUsers);
    } else {
      throw new RuntimeException("持有藏品不可为空");
    }
  }

  @ApiOperation(value = "根据拉新排名查询用户列表-无分页")
  @PostMapping("/findUserByTop")
  public ApiResponse<List<SysUser>> findUserByTop(@RequestBody TopSelectParam param) {
    if (param.getStartTime() == null || param.getEndTime() == null)
      throw new RuntimeException("开始时间截止时间必传");
    List<SysUser> sysUsers = sysUserService.findUserByTop(param);
    return ApiResponse.success(sysUsers);
  }

  @Autowired private SysUserService sysUserService;

  @ApiOperation(value = "根据条件查询用户列表-无分页")
  @PostMapping("/findByParam")
  public ApiResponse<List<SysUser>> findByParam(@RequestBody SysUser user) {
    LambdaQueryWrapper<SysUser> que = Wrappers.lambdaQuery(SysUser.class);
    if (Strings.isNotEmpty(user.getUsername())) {
      que.like(SysUser::getUsername, user.getUsername());
    }
    if (Strings.isNotEmpty(user.getPhone())) {
      que.like(SysUser::getPhone, user.getPhone());
    }
    if (Strings.isNotEmpty(user.getUserIndex())) {
      que.eq(SysUser::getUserIndex, user.getUserIndex());
    }
    if (Strings.isNotEmpty(user.getRealName())) {
      que.eq(SysUser::getRealName, user.getRealName());
    }
    if (Strings.isNotEmpty(user.getIdCard())) {
      que.like(SysUser::getIdCard, user.getIdCard());
    }
    que.ne(BaseEntity::getState, -1);
    List<SysUser> sysUsers = sysUserService.findByParam(que);
    return ApiResponse.success(sysUsers);
  }
}
