package com.qy.ntf.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseController;
import com.qy.ntf.base.BaseEntity;
import com.qy.ntf.bean.dto.*;
import com.qy.ntf.bean.entity.Menu;
import com.qy.ntf.bean.entity.Role;
import com.qy.ntf.bean.entity.SysUserAdmin;
import com.qy.ntf.bean.param.*;
import com.qy.ntf.service.*;
import com.qy.ntf.util.ApiResponse;
import com.qy.ntf.util.EncryptUtil;
import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.llPay.YTHttpHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.controller @ClassName:
 * SystemController @Author: 王振读 @Description: ${description} @Date: 2021/11/26 9:31 @Version: 1.0
 */
@RestController
@RequestMapping("/system")
// @CrossOrigin(origins = "*")
@Slf4j
@Api(tags = {"ADMIN-系统管理"})
public class SystemController extends BaseController {
  @Autowired private SysUserService userService;
  @Autowired private RoleService roleService;
  @Autowired private OrganizationService organizationService;
  @Autowired private SysUserAdminService sysUserAdminService;

  @Value("${password-key}")
  private String passwordKey;

  @Autowired private MenuService menuService;

  @ApiOperation(value = "ADMIN - " + "菜单管理 - 修改菜单状态（逻辑删除）")
  @PostMapping("/menu/edit/state")
  public ApiResponse<Void> editState(@RequestBody MenuDto dto) {
    menuService.updateState(dto.getId(), -1);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "菜单管理 - 修改菜")
  @PostMapping("/menu/edit")
  public ApiResponse<Void> editMenu(@RequestBody MenuDto dto) {
    menuService.update(dto);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "角色管理 - 保存角色菜单")
  @PostMapping("/role/savemenus")
  public ApiResponse<Void> saveRoleMenus(@RequestBody RoleMenuDto dto) {
    menuService.updateRoleMenus(dto.getRoleId(), dto.getMenuIds());
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "菜单管理 - 添加菜单")
  @PostMapping("/menu/add")
  public ApiResponse<Void> addMenu(@RequestBody MenuDto param) {
    SysUserAdminDto userData = getAdminUserData();
    param.setCreateId(userData.getId());
    param.setCreateTime(new Date());
    param.setUpdateId(userData.getId());
    param.setUpdateTime(new Date());
    List<MenuDto> menuList = menuService.getMenuList(param.getPid());
    for (MenuDto menu : menuList) {
      if (menu.getSortNumber() >= param.getSortNumber()) {
        menuService.updateMenuSortNumber(menu.getId(), (menu.getSortNumber() + 1));
      }
    }
    menuService.insertData(param, Menu.class);
    return ApiResponse.success();
  }
  // 按上级用户可以处理下级用户的增删改
  @ApiOperation(value = "ADMIN - " + "管理员管理 - (分页显示)")
  @GetMapping("/user/list/{page}/{size}")
  public ApiResponse<IPage<SysUserAdminDto>> list(
      @PathVariable("page") long page, @PathVariable("size") long size) {
    LambdaQueryWrapper<SysUserAdmin> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SysUserAdmin::getState, 1).or(wrapper -> wrapper.eq(BaseEntity::getState, 0));

    IPage<SysUserAdminDto> list =
        sysUserAdminService.selectPageList(SysUserAdminDto.class, page, size, queryWrapper);
    // 去掉密码
    list.getRecords().forEach(user -> user.setAdminPass(""));
    return ApiResponse.success(list);
  }

  @ApiOperation(value = "ADMIN - " + "角色管理列表 不分页")
  @GetMapping("/role/list")
  public ApiResponse<List<RoleDto>> list() {
    LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Role::getState, 1);
    List<RoleDto> list = roleService.selectList(RoleDto.class, queryWrapper);
    return ApiResponse.success(list);
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 修改管理员状态（逻辑删除）")
  @PostMapping("/user/edit/state")
  public ApiResponse<Void> editState(@RequestBody SysUserAdminDto user) {
    sysUserAdminService.updateState(user);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 获取指定管理员角色")
  @GetMapping("/user/userroles/{userId}")
  public ApiResponse<List<RoleDto>> getUserRoles(@PathVariable("userId") Long userId) {
    List<RoleDto> roles = roleService.getUserRoles(userId);
    return ApiResponse.success(roles);
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 检查管理员名是否重复 ")
  @PostMapping("/user/checkUsername")
  public ApiResponse<Object> checkUserName(@RequestBody SysUserAdmin user) {
    return userService.checkUserName(user);
  }

  @ApiOperation(value = "ADMIN - " + "添加角色 ")
  @PostMapping("/user/addRole")
  public ApiResponse<Object> addRole(@RequestBody AddRoleParam role) {
    return userService.addRole(role, getAdminUserData());
  }

  @ApiOperation(value = "ADMIN - " + "修改管理员密码 ")
  @PostMapping("/admin/updateAdminPass")
  public ApiResponse<Object> updateAdminPass(@RequestBody UpdateAdminParam updateAdminParam) {
    return userService.updateAdminPass(updateAdminParam, getAdminUserData());
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 检查管理员名是否重复 ")
  @PostMapping("/user/checkUserAccount")
  public ApiResponse<Object> checkUserAccount(@RequestBody SysUserAdmin user) {
    return userService.checkUserAccount(user);
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 修改管理员")
  @PostMapping("/user/edit")
  public ApiResponse<Void> list(@RequestBody SysUserAdminDto user) {
    SysUserAdminDto userDto =
        sysUserAdminService.selectDataById(SysUserAdminDto.class, user.getId()).get();
    user.setState(userDto.getState());
    user.setUpdateId(userDto.getUpdateId());
    user.setUpdateTime(new Date());
    user.setCreateId(userDto.getCreateId());
    user.setCreateTime(userDto.getCreateTime());
    user.setAdminPass(userDto.getAdminPass());
    if (!sysUserAdminService.selectDataById(SysUserAdminDto.class, user.getId()).isPresent()) {
      return ApiResponse.fail("不存在的管理员");
    }
    sysUserAdminService.updateDataById(user.getId(), user);
    //    sysUserAdminService.updateAdminRoleIds(user);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 添加管理员")
  @PostMapping("user/add")
  public ApiResponse<String> add(@RequestBody SysUserAdminDto user) {

    SysUserAdminDto userData = getAdminUserData();
    user.setAdminPass(EncryptUtil.getInstance().MD5("123456", passwordKey));
    user.setCreateTime(new Date());
    user.setCreateId(userData.getId());
    user.setState(1);
    user.setUpdateId(userData.getId());
    user.setUpdateTime(new Date());
    List<SysUserAdmin> sysUserAdmins = sysUserAdminService.selectByAccount(user.getAdminAccount());
    if (sysUserAdmins.size() > 0) {
      return ApiResponse.fail("该账户已被占用");
    }
    sysUserAdminService.insertData(user, SysUserAdmin.class);
    return ApiResponse.success("初始密码123456");
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 修改管理员角色")
  @PostMapping("user/updateRoles")
  public ApiResponse<String> updateRoles(@RequestBody UpdateAdminRoleParam param) {

    sysUserAdminService.updateRoles(param);
    return ApiResponse.success("success");
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 按 id 删除管理员")
  @DeleteMapping("/user/delete/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) throws Exception {
    sysUserAdminService.deleteDataById(id);
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 按 id (解）冻结管理员")
  @DeleteMapping("/user/stop/{id}")
  public ApiResponse<Void> stop(@PathVariable("id") Long id) throws Exception {
    Optional<SysUserAdminDto> sysUserAdminDto =
        sysUserAdminService.selectDataById(SysUserAdminDto.class, id);
    if (sysUserAdminDto.isPresent()) {
      SysUserAdminDto record = sysUserAdminDto.get();
      record.setState(record.getState() == 0 ? 1 : 0);
      sysUserAdminService.updateDataById(id, record);
      return ApiResponse.success();
    }
    throw new RuntimeException("无效的id");
  }

  @ApiOperation(value = "ADMIN - " + "管理员管理 - 重置管理员密码")
  @PostMapping("/user/reset-password/{userId}")
  public ApiResponse<Void> resetPassword(@PathVariable("userId") Long userId) {
    sysUserAdminService.resetPassword(userId, EncryptUtil.getInstance().MD5("123456", passwordKey));
    return ApiResponse.success();
  }

  @ApiOperation(value = "ADMIN - " + "菜单管理 - 获取所有菜单")
  @GetMapping("/menu/all")
  public ApiResponse<List<MenuDto>> allMenus() {
    List<MenuDto> allMenus = null;

    // 超级管理员
    if (isSuperMan()) {
      allMenus = menuService.getAllMenus();
    } else {
      // 普通管理员(租户管理员也是普通用户)
      SysUserAdminDto userData = getAdminUserData();
      allMenus = menuService.getTenantMenus(userData.getId());
    }

    return ApiResponse.success(allMenus);
  }

  @ApiOperation(value = "ADMIN - " + "角色管理 - 获取角色的所有菜单")
  @GetMapping("/role/menus/{roleId}")
  public ApiResponse<List<MenuDto>> getRoleMenus(@PathVariable("roleId") Long roleId) {
    List<MenuDto> list = menuService.getRoleMenus(roleId);
    return ApiResponse.success(list);
  }

  @ApiOperation(value = "ADMIN - " + "组织机构管理 - 修改组织机构", hidden = true)
  @PostMapping("/organization/edit")
  public ApiResponse<Void> editOrganization(@RequestBody OrganizationDto organization) {
    try {
      organizationService.updateDataById(organization.getId(), organization);
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.fail("修改组织机构失败");
    }
    return ApiResponse.success();
  }

  @ApiOperation(
      value = "ADMIN - " + "组织机构管理 - 添加组织机构",
      notes = "添加租户时，code参数设为null, 这种情况由超级管理员调用；其它的情况code不能为null",
      hidden = true)
  @PostMapping("/organization/add")
  public ApiResponse<Void> addOrganization(@RequestBody OrganizationDto organization) {
    try {
      organizationService.save(organization);
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.fail("添加失败");
    }
    return ApiResponse.success();
  }

  @ApiOperation(value = "组织机构管理 - 获取当前用户的整个组织机构树", notes = "根据登录用户的组织机构代码的前4位，生成机构树", hidden = true)
  @GetMapping("/organization/all")
  public ApiResponse<List<OrganizationDto>> getOrganizationTree() {
    UserDto userData = getUserData();
    return ApiResponse.success(userService.organizationTree("123456".substring(0, 4)));
  }

  @Autowired private OrderProductService orderProductService;

  @ApiOperation(value = "APP - 获取签约的银行卡列表", notes = "获取签约的银行卡列表")
  @GetMapping("/getBin")
  public ApiResponse<Object> getBin() {
    UserDto userData = getUserData();
    String url = "https://queryapi.lianlianpay.com/bankcardbindlist.htm";
    QueryBinParam queryBinParam = new QueryBinParam();
    queryBinParam.setOid_partner(ResourcesUtil.getProperty("lianlian_oid"));
    queryBinParam.setUser_id(userData.getId() + "");

    queryBinParam.setSign(
        orderProductService.genSign(JSON.parseObject(JSON.toJSONString(queryBinParam))));
    String reqJson = JSON.toJSONString(queryBinParam);
    log.info("请求报文为:" + reqJson);
    String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, url);
    log.info("结果报文为:" + resJson);
    return ApiResponse.success(JSONObject.parseObject(resJson));
  }

  @ApiOperation(value = "APP - 连连pay解约银行卡", notes = "连连pay解约银行卡")
  @PostMapping("/unbin")
  public ApiResponse<Object> unbin(@RequestBody UnBinParam param) {
    UserDto userData = getUserData();
    String url = "https://traderapi.lianlianpay.com/bankcardunbind.htm";
    UnBinParam unBinParam = new UnBinParam();
    unBinParam.setOid_partner(ResourcesUtil.getProperty("lianlian_oid"));
    unBinParam.setUser_id(userData.getId() + "");
    unBinParam.setNo_agree(param.getNo_agree());
    unBinParam.setSign(
        orderProductService.genSign(JSON.parseObject(JSON.toJSONString(unBinParam))));
    String reqJson = JSON.toJSONString(unBinParam);
    log.info("请求报文为:" + reqJson);
    String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, url);
    return ApiResponse.success(JSONObject.parseObject(resJson));
  }
}
