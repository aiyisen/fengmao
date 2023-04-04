package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.StoreCategroyDto;
import com.qy.ntf.bean.dto.StoreCategroyAddDto;
import com.qy.ntf.bean.dto.StoreCategroyUpdateDto;
import com.qy.ntf.bean.entity.StoreCategroy;
import com.qy.ntf.base.BaseService;
import java.util.List;
/**
 * @author 王振读
 * 2022-08-06 13:01:44
 * DESC : 藏品分类 service服务
 */
public interface StoreCategroyService extends BaseService<StoreCategroyDto,StoreCategroy> {

    /**
    * 主键详情
    * @param id
    * @return
    */
        StoreCategroyDto getStoreCategroyById(Long id);

    /**
    * 保存方法
    * @param dto
    */
    void save(StoreCategroyAddDto dto);

    /**
    * 更新方法
    * @param dto
    */
    void update(StoreCategroyUpdateDto dto);

    /**
    * 更新状态方法
    * @param dto
    */
    void updateState(StoreCategroyDto dto);

    /**
    * 删除方法
    * @param id
    */
    void delete(Long id);

    /**
   * 列表查询
   * @param dto
   * @return
   */
    List<StoreCategroyDto> list(StoreCategroyDto dto);

    /**
    * 常规分页查询
    * @param currentPage 当前页数 （第一页或者第三页）
    * @param pageSize 页数大小（每页记录数）
    * @param queryWrapper 查询条件
    * @return 分页的设备参数
    */
    IPage<StoreCategroyDto> getListByPage(Class<StoreCategroyDto> clazz, long currentPage, Long pageSize, LambdaQueryWrapper<StoreCategroy> queryWrapper);

}

