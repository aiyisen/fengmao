package com.qy.ntf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qy.ntf.base.BaseService;
import com.qy.ntf.bean.dto.OrganizationDto;
import com.qy.ntf.bean.entity.Organization;
import com.qy.ntf.dao.OrganizationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class OrganizationService implements BaseService<OrganizationDto, Organization> {

	@Autowired
	private OrganizationDao organizationDao;

	public OrganizationDao getDao() {
		return this.organizationDao;
	}

	public String getNextCode(String parent) {
		return getOrganizationCode(parent);
	}
	
	// 取一级组织，即租户列表
	public IPage<OrganizationDto> organizations(long currentPage, Long pageSize) {
		LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.likeRight(Organization::getCode, "A");
		queryWrapper.apply("length(code) = 4");
		queryWrapper.orderByAsc(Organization::getCode);
		return selectPageList(OrganizationDto.class, currentPage, pageSize, queryWrapper);
	}

	
	// 取当前用户的整个组织机构, 不管是不是管理员
	public List<OrganizationDto> organizationTree(String root) {
		LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Organization::getCode, root);
		List<OrganizationDto> list = selectList(OrganizationDto.class, queryWrapper);
		
		list.forEach(organization -> {
			List<OrganizationDto> children = childrenOrganization(organization.getCode());
			if (children.size() > 0) {
				organization.setChildren(children);
			}
		});
		return list;
	}

	private List<OrganizationDto> childrenOrganization(String code) {

		LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByAsc(Organization::getCode);
		// 二级及三级，四级。。。 查询条件： like 'xxxx%' and length(code) = (参数代码长度 + 4)
		queryWrapper.likeRight(Organization::getCode, code);
		queryWrapper.apply("length(code)=" + (code.length() + 4));
		
		List<OrganizationDto> list = selectList(OrganizationDto.class, queryWrapper);

		list.forEach(organization -> {
			List<OrganizationDto> children = childrenOrganization(organization.getCode());
			if (children.size() > 0) {
				organization.setChildren(children);
			}
		});
		
		return list;
	}

	public void deleteOrganizationById(Long id) throws Exception {
		Organization organization = organizationDao.selectById(id);
		if (organization == null)
			return; // 没有数据，就当成功了吧。

		// where coode like 'XXXX%' and length(code) > len(XXXX)
		LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.likeRight(Organization::getCode, organization.getCode());
		queryWrapper.apply("length(code) > " + organization.getCode().length());
		Integer count = organizationDao.selectCount(queryWrapper);

		// count > 0, 说明有子元素，不能删除
		if (count > 0) {
			throw new Exception("该组织机构有下级组织机构，不能删除");
		}

		this.deleteDataById(id);
	}
	
	@Transactional
	public void save(OrganizationDto dto) throws Exception {
		String parentCode = dto.getCode();   // 上级组织机构代码，用code字段存放
		String code = getOrganizationCode(parentCode);
		dto.setCode(code);
		insertData(dto, Organization.class);
	}

	// 根据上级组织机构代码，自动生成新的组织机构代码
	private String getOrganizationCode(String parent) {
		
		// 一级组织机构， parent传入0
		String code = null; // 这个是返回结果
		
		if (parent == null || parent.length() == 0)
			parent = "A";

		int codeLen = 4; // 要生成的组织机构代码长度
		if (parent.length() > 3) {
			codeLen = parent.length() + 4;
		}

		Organization organization = organizationDao.getMaxCode(parent, codeLen);
		
		if (organization == null) {
			code = (parent.length() > 3 ? parent : "") + charMap.get(codeLen) + "001";
		} else {
			// 取最右边3个数字，加1
			String maxCode = organization.getCode();
			String seq = String.format("%03d", Integer.parseInt(maxCode.substring(codeLen - 3)) + 1);
			code = maxCode.substring(0, codeLen - 3) + seq;
		}
		
		return code;
	}

	// 按这个map, 最高级十级的组织机构
	private static Map<Integer, String> charMap = new TreeMap<Integer, String>() {
		private static final long serialVersionUID = -3L;
		{
			put(4, "A");
			put(8, "B");
			put(12, "C");
			put(16, "D");
			put(20, "E");
			put(24, "F");
			put(28, "G");
			put(32, "H");
			put(36, "I");
			put(40, "J");
		}
	};

	public void updateState(Long id, Integer state) {
		organizationDao.updateState(id, state);
	}
}
