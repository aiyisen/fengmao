package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.bean.dto.StoreProPoolReadRecordDto;
import com.qy.ntf.bean.dto.StoreProPoolReadRecordAddDto;
import com.qy.ntf.bean.dto.StoreProPoolReadRecordUpdateDto;
import com.qy.ntf.bean.entity.StoreProPoolReadRecord;
import com.qy.ntf.base.BaseService;
import java.util.List;
/**
 * @author 王振读
 * 2022-05-28 00:02:44
 * DESC :  service服务
 */
public interface StoreProPoolReadRecordService extends BaseService<StoreProPoolReadRecordDto,StoreProPoolReadRecord> {

    /**
    * 主键详情
    * @param id
    * @return
    */
        StoreProPoolReadRecordDto getStoreProPoolReadRecordById(Long id);

    /**
    * 保存方法
    * @param dto
    */
    void save(StoreProPoolReadRecordAddDto dto);

    /**
    * 更新方法
    * @param dto
    */
    void update(StoreProPoolReadRecordUpdateDto dto);

    /**
    * 更新状态方法
    * @param dto
    */
    void updateState(StoreProPoolReadRecordDto dto);

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
    List<StoreProPoolReadRecordDto> list(StoreProPoolReadRecordDto dto);

    /**
    * 常规分页查询
    * @param currentPage 当前页数 （第一页或者第三页）
    * @param pageSize 页数大小（每页记录数）
    * @param queryWrapper 查询条件
    * @return 分页的设备参数
    */
    IPage<StoreProPoolReadRecordDto> getListByPage(Class<StoreProPoolReadRecordDto> clazz, long currentPage, Long pageSize, LambdaQueryWrapper<StoreProPoolReadRecord> queryWrapper);

}

