package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.ButtonPermissionsDto;
import com.qy.ntf.bean.dto.DynamicRoutingDto;
import com.qy.ntf.bean.dto.MenuDto;
import com.qy.ntf.bean.entity.Menu;
import com.qy.ntf.dao.MenuDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MenuService implements BaseService<MenuDto, Menu> {

  @Autowired private MenuDao menuDao;

  @Override
  public MenuDao getDao() {
    return this.menuDao;
  }

  // 根据pid取某一级菜单
  public List<MenuDto> getMenuList(Long pid) {
    LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
    // where pid=... order by id asc
    queryWrapper.eq(Menu::getPid, pid);
    queryWrapper.orderByAsc(Menu::getSortNumber);
    queryWrapper.orderByAsc(Menu::getName);
    queryWrapper.eq(Menu::getState, 1);
    return selectList(MenuDto.class, queryWrapper);
  }

  public List<MenuDto> getAllMenus() {
    return getChildren(0L);
  }

  // 租户管理员的菜单, 树
  public List<MenuDto> getTenantMenus(Long userId) {
    return getTenantMenus(userId, 0L);
  }

  private List<MenuDto> getTenantMenus(Long userId, Long pid) {
    ModelMapper modelMapper = new ModelMapper();

    List<Menu> menus = menuDao.getTenantMenu(userId, pid);

    return menus.stream()
        .map(
            menu -> {
              MenuDto dto = modelMapper.map(menu, MenuDto.class);

              List<MenuDto> children = getTenantMenus(userId, dto.getId());
              if (children.size() > 0) {
                dto.setChildren(children);
              }

              return dto;
            })
        .collect(Collectors.toList());
  }

  // 递归
  private List<MenuDto> getChildren(Long pid) {
    List<MenuDto> list = getMenuList(pid);
    list.forEach(
        dto -> {
          List<MenuDto> children = getChildren(dto.getId());
          if (children.size() > 0) {
            dto.setChildren(children);
          }
        });

    return list;
  }

  // 获取角色的所有菜单， 列表返回，不是树，用于前端遍历。
  public List<MenuDto> getRoleMenus(Long roleId) {
    ModelMapper modelMapper = new ModelMapper();
    List<Menu> menus = menuDao.getRoleMenus(roleId);
    List<MenuDto> list =
        menus.stream()
            .map(menu -> modelMapper.map(menu, MenuDto.class))
            .collect(Collectors.toList());
    return list;
  }

  // 取用户所有菜单
  public List<MenuDto> getUserMenus(Long userId) {
    List<Menu> list = menuDao.getUserMenus(userId);
    ModelMapper modelMapper = new ModelMapper();

    return list.stream()
        .map(menu -> modelMapper.map(menu, MenuDto.class))
        .collect(Collectors.toList());
  }

  // 修改角色菜单，先删除后加插入
  @Transactional
  public void updateRoleMenus(Long roleId, List<Long> menuIds) {
    menuDao.deleteRoleMenu(roleId);
    menuIds.stream().forEach(menuId -> menuDao.insertRoleMenu(roleId, menuId));
  }

  public int updateMenuSortNumber(Long id, Integer sortNumber) {
    return menuDao.updateMenuSortNumber(id, sortNumber);
  }

  public List<MenuDto> getMenuListExclusiveID(Long pid, Long id) {
    LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
    // where pid=... order by id asc
    queryWrapper.eq(Menu::getPid, pid);
    queryWrapper.orderByAsc(Menu::getSortNumber);
    queryWrapper.orderByAsc(Menu::getName);
    queryWrapper.ne(Menu::getId, id);
    return selectList(MenuDto.class, queryWrapper);
  }

  public void updateState(Long id, Integer state) {
    menuDao.updateState(id, state);
  }

  /** */
  public List<Map<String, Object>> getDynamicRoutingList(Long adminId) {
    // 组装头部
    List<Map<String, Object>> resultList = new ArrayList<>();
    Map<String, Object> map = new ConcurrentHashMap<String, Object>();
    map.put("router", "root");
    List<Map<String, Object>> menuList = new ArrayList<>();
    map.put("children", menuList);
    resultList.add(map);

    List<DynamicRoutingDto> dynamicRoutingList = menuDao.getDynamicRoutingList(adminId, 0);

    String flagName = "";

    if (dynamicRoutingList.size() > 0) {
      flagName = dynamicRoutingList.get(0).getPname();
    }

    Map<String, Object> routingMap = new ConcurrentHashMap<String, Object>();

    List<Map<String, Object>> childrenList = new ArrayList<>();

    for (DynamicRoutingDto routhing : dynamicRoutingList) {
      if (flagName.equals(routhing.getPname())) {
        routingMap.put("router", routhing.getPpath());
        routingMap.put("name", routhing.getPname());
        routingMap.put("icon", routhing.getPicon());
        routingMap.put("authority", routhing.getPpath());
        Map<String, Object> map2 = new ConcurrentHashMap<String, Object>();
        map2.put("router", routhing.getPath());
        map2.put("name", routhing.getName());
        map2.put("icon", routhing.getIcon());
        map2.put("authority", routhing.getPath());
        List<Map<String, Object>> childrenList2 =
            this.getChildrenList(adminId, routhing.getId(), routhing.getName());
        if (childrenList2.size() > 0) {
          map2.put("children", childrenList2);
        }
        childrenList.add(map2);
      } else {
        routingMap.put("children", childrenList);
        menuList.add(routingMap);
        flagName = routhing.getPname();
        childrenList = new ArrayList<>();
        routingMap = new ConcurrentHashMap<String, Object>();

        routingMap.put("router", routhing.getPpath());
        routingMap.put("name", routhing.getPname());
        routingMap.put("icon", routhing.getPicon());
        routingMap.put("authority", routhing.getPpath());
        Map<String, Object> map2 = new ConcurrentHashMap<String, Object>();
        map2.put("router", routhing.getPath());
        map2.put("name", routhing.getName());
        map2.put("icon", routhing.getIcon());
        map2.put("authority", routhing.getPath());
        List<Map<String, Object>> childrenList2 =
            this.getChildrenList(adminId, routhing.getId(), routhing.getName());
        if (childrenList2.size() > 0) {
          map2.put("children", childrenList2);
        }
        childrenList.add(map2);
      }
    }
    routingMap.put("children", childrenList);
    menuList.add(routingMap);
    return resultList;
  }

  public List<Map<String, Object>> getChildrenList(Long adminId, Integer pid, String name) {

    List<Map<String, Object>> childrenList = new ArrayList<>();
    List<DynamicRoutingDto> dynamicRoutingList =
        menuDao.getDynamicRoutingListByName(adminId, pid, name);
    if (dynamicRoutingList.size() == 0) {
      return childrenList;
    }
    String flagName = "";

    flagName = dynamicRoutingList.get(0).getPname();

    Map<String, Object> routingMap = new ConcurrentHashMap<>();

    for (DynamicRoutingDto routhing : dynamicRoutingList) {
      if (flagName.equals(routhing.getPname())) {
        routingMap.put("router", routhing.getPath());
        routingMap.put("name", routhing.getName());
        routingMap.put("icon", routhing.getIcon());
        routingMap.put("authority", routhing.getPath());
        List<Map<String, Object>> childrenList2 =
            this.getChildrenList(adminId, routhing.getId(), routhing.getName());
        if (childrenList2.size() > 0) {
          routingMap.put("children", childrenList2);
        }
        childrenList.add(routingMap);
        routingMap = new ConcurrentHashMap<>();
      } else {

        flagName = routhing.getPname();
        childrenList = new ArrayList<>();
        routingMap = new ConcurrentHashMap<>();

        routingMap.put("router", routhing.getPath());
        routingMap.put("name", routhing.getName());
        routingMap.put("icon", routhing.getIcon());
        routingMap.put("authority", routhing.getPath());
        List<Map<String, Object>> childrenList2 =
            this.getChildrenList(adminId, routhing.getId(), routhing.getName());
        if (childrenList2.size() > 0) {
          routingMap.put("children", childrenList2);
        }
        childrenList.add(routingMap);
        routingMap = new ConcurrentHashMap<>();
      }
    }
    return childrenList;
  }

  public List<Map<String, Object>> getButtonPermissions(Long userId) {
    List<Map<String, Object>> resultList = new ArrayList<>();

    Map<String, Object> map = new ConcurrentHashMap<String, Object>();
    List<Object> list = new ArrayList<Object>();
    String pathFlag = null;
    List<ButtonPermissionsDto> buttonPermissions = menuDao.getButtonPermissions(userId);
    if (buttonPermissions.size() > 0) {
      pathFlag = buttonPermissions.get(0).getMpath();
    }

    for (ButtonPermissionsDto b : buttonPermissions) {
      if (pathFlag.equals(b.getMpath())) {
        map.put("id", b.getMpath());
        list.add(b.getBpath());
      } else {
        map.put("operation", list);
        resultList.add(map);
        map = new ConcurrentHashMap<String, Object>();
        list = new ArrayList<Object>();
        pathFlag = b.getMpath();
        map.put("id", b.getMpath());
        list.add(b.getBpath());
      }
    }
    map.put("operation", list);
    resultList.add(map);
    return resultList;
  }

  public void update(MenuDto menu) {
    ModelMapper md = new ModelMapper();
    Menu sysMenu = new Menu();
    md.map(menu, sysMenu);
    menuDao.updateById(sysMenu);
  }
}
