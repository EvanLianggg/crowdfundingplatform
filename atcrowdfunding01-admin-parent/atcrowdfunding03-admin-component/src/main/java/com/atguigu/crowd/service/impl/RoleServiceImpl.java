package com.atguigu.crowd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import com.atguigu.crowd.entity.RoleExample.Criteria;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
		
		// 1. Turn on the paging function
		PageHelper.startPage(pageNum, pageSize);
		
		// 2. Execute query
		List<Role> roleList = roleMapper.selectRoleByKeyword(keyword);
		
		// 3. Encapsulated as PageInfo object return
		return new PageInfo<>(roleList);
	}

	@Override
	public void saveRole(Role role) {
		roleMapper.insert(role);
	}

	@Override
	public void updateRole(Role role) {
		roleMapper.updateByPrimaryKey(role);
	}

	@Override
	public void removeRole(List<Integer> roleIdList) {
		
		RoleExample example = new RoleExample();
		
		Criteria criteria = example.createCriteria();
		
		//delete from t_role where id in (5,8,12)
		criteria.andIdIn(roleIdList);
		
		roleMapper.deleteByExample(example);
	}

	@Override
	public List<Role> getAssignedRole(Integer adminId) {
		
		return roleMapper.selectAssignedRole(adminId);
	}

	@Override
	public List<Role> getUnAssignedRole(Integer adminId) {
		return roleMapper.selectUnAssignedRole(adminId);
	}

}
