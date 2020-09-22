package com.atguigu.crowd.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private AuthMapper authMapper;

	@Override
	public List<Auth> getAll() {
		return authMapper.selectByExample(new AuthExample());
	}

	@Override
	public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
		return authMapper.selectAssignedAuthIdByRoleId(roleId);
	}

	@Override
	public void saveRoleAuthRelathinship(Map<String, List<Integer>> map) {
		
		// 1. Get the value of roleId
		List<Integer> roleIdList = map.get("roleId");
		Integer roleId = roleIdList.get(0);
		
		// 2. Delete the old relationship data
		authMapper.deleteOldRelationship(roleId);
		
		// 3. Get authIdList
		List<Integer> authIdList = map.get("authIdArray");
		
		// 4. Determine whether authIdList is valid
		if(authIdList != null && authIdList.size() > 0) {
			authMapper.insertNewRelationship(roleId, authIdList);
		}
	}

	@Override
	public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
		
		return authMapper.selectAssignedAuthNameByAdminId(adminId);
	}

}
