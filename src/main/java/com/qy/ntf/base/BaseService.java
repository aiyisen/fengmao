package com.qy.ntf.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// DTO: 用于数据传输，主要用于controller层
// ENTITY: 用于dao与数据库打交道
public interface BaseService<DTO, ENTITY> {

	// 插入数据
	default ENTITY insertData(DTO dto, Class<ENTITY> clazz, ModelMapper modelMapper) {
		ENTITY entity = modelMapper.map(dto, clazz);
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		int insert = getDao().insert(entity);
		return entity;
	}

	// 插入数据
	default ENTITY insertData(DTO dto, Class<ENTITY> clazz) {
		// 修改匹配策略为严格
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return insertData(dto, clazz, modelMapper);
	}

	
	//  根据条件修改数据 - 自定义转换类
	default void updateDataByCondition(DTO dto, LambdaQueryWrapper<ENTITY> queryWrapper, ModelMapper modelMapper) {
		getDao().selectList(queryWrapper).stream().findFirst().ifPresent(s -> {
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			modelMapper.map(dto, s);
			getDao().updateById(s);
		});
	}

	// 根据条件修改数据
	default void updateDataByCondition(DTO dto, LambdaQueryWrapper<ENTITY> queryWrapper) {
		// 修改匹配策略为严格
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		updateDataByCondition(dto, queryWrapper,modelMapper);
	}

	
	 // 根据Id修改数据 - 自定义转换类
	default void updateDataById(Long id, DTO dto, ModelMapper modelMapper) {
		Optional.ofNullable(getDao().selectById(id)).ifPresent(s -> {
			modelMapper.map(dto, s);
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			getDao().updateById(s);
		});
	}

	//  根据Id修改数据
	default void updateDataById(Long id, DTO dto) {
		// 修改匹配策略为严格
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		updateDataById(id, dto, modelMapper);
	}

	default void deleteDataById(Long id) {
		getDao().deleteById(id);
	}

	default Optional<DTO> selectDataByCondition(Class<DTO> clazz, LambdaQueryWrapper<ENTITY> queryWrapper,
                                                ModelMapper modelMapper) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return getDao().selectList(queryWrapper).stream().findFirst().map(s -> modelMapper.map(s, clazz));
	}

	default Optional<DTO> selectDataByCondition(Class<DTO> clazz, LambdaQueryWrapper<ENTITY> queryWrapper) {
		ModelMapper modelMapper = new ModelMapper();

		// 修改匹配策略为严格
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return selectDataByCondition(clazz, queryWrapper, modelMapper);
	}

	default Optional<DTO> selectDataById(Class<DTO> clazz, Long id, ModelMapper modelMapper) {
		// 修改匹配策略为严格
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return Optional.ofNullable(getDao().selectById(id)).map(s -> modelMapper.map(s, clazz));
	}

	default Optional<DTO> selectDataById(Class<DTO> clazz, Long id) {
		ModelMapper modelMapper = new ModelMapper();

		// 修改匹配策略为严格
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return selectDataById(clazz, id, modelMapper);
	}

	default Optional<DTO> selectDataByIdOneToTwo(Class<DTO> clazz, Long id) {
		ModelMapper modelMapper = new ModelMapper();

		// 修改匹配策略为严格
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		//modelMapper.map(source, target);
		return selectDataById(clazz, id,modelMapper);
	}
//	default Page<DTO> selectPageDataListByCondition(Class<DTO> clazz, Long currentPage, Long pageSize,
//			LambdaQueryWrapper<ENTITY> queryWrapper, ModelMapper modelMapper) {
//		return getDao().selectPage(new Page<>(currentPage, pageSize), queryWrapper)
//				.map(s -> modelMapper.map(s, clazz));
//	}
	
	
	// 根据查询条件，返回转换成dto的结果，分页
	default IPage<DTO> selectPageList(Class<DTO> clazz, long currentPage, Long pageSize, LambdaQueryWrapper<ENTITY> queryWrapper) {
		final ModelMapper modelMapper = new ModelMapper();
		// 修改匹配策略为严格
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Page<ENTITY> page = getDao().selectPage(new Page<ENTITY>(currentPage, pageSize), queryWrapper);
		return page.convert(e -> modelMapper.map(e, clazz));
	}
	
	// 根据查询条件，返回转换成dto的结果，不分页
	default List<DTO> selectList(Class<DTO> clazz, LambdaQueryWrapper<ENTITY> queryWrapper) {
		final ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		List<ENTITY> list = getDao().selectList(queryWrapper);
		return list.stream().map(e -> modelMapper.map(e, clazz)).collect(Collectors.toList());
	}

//	default Page<DTO> selectPageDataListByCondition(Class<DTO> clazz, Long currentPage, Long pageSize,
//			LambdaQueryWrapper<ENTITY> queryWrapper) {
//		return selectPageDataListByCondition(clazz, currentPage, pageSize, queryWrapper, new ModelMapper());
//	}

	BaseMapper<ENTITY> getDao();
}
