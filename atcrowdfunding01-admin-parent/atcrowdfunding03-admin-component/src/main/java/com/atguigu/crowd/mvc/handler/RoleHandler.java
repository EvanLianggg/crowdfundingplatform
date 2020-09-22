package com.atguigu.crowd.mvc.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;

@RestController
public class RoleHandler {
	
	@Autowired
	private RoleService roleService;
	
	// @ResponseBody
	@RequestMapping("/role/remove/by/role/id/array.json")
	public ResultEntity<String> removeByRoleIdAarry(@RequestBody List<Integer> roleIdList) {
		
		roleService.removeRole(roleIdList);
		
		return ResultEntity.successWithoutData();
	}
	
	// @ResponseBody
	@RequestMapping("/role/update.json")
	public ResultEntity<String> updateRole(Role role) {
		
		roleService.updateRole(role);
		
		return ResultEntity.successWithoutData();
	}
	
	// @ResponseBody
	@RequestMapping("/role/save.json")
	public ResultEntity<String> saveRole(Role role) {
		
		roleService.saveRole(role);
		
		return ResultEntity.successWithoutData();
	}
	
	@PreAuthorize("hasRole('部长')")
	// @ResponseBody
	@RequestMapping("/role/get/page/info.json")
	public ResultEntity<PageInfo<Role>> getPageInfo(
				@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
				@RequestParam(value="pageSize", defaultValue="5") Integer pageSize,
				@RequestParam(value="keyword", defaultValue="") String keyword
			) {
		
				// Call the Service method to get paging data
		PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);
		
		// Encapsulate in the ResultEntity object and return (if the above operation throws an exception, hand it over to the exception mapping mechanism)
		return ResultEntity.successWithData(pageInfo);
	}

}
