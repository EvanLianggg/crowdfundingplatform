package com.atguigu.crowd.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;

@Component
public class CrowdUserDetailsService implements UserDetailsService {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthService authService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// 1. Query the Admin object based on the account name
		Admin admin = adminService.getAdminByLoginAcct(username);
		
		// 2. Get adminId
		Integer adminId = admin.getId();
		
		// 3. Query role information based on adminId
		List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
		
		// 4. Query permission information based on adminId
		List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);
		
		// 5. Create a collection object to store GrantedAuthority
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		// 6. Traverse assignedRoleList to save role information
		for (Role role : assignedRoleList) {
			
			// Note: Don't forget to add the prefix!
			String roleName = "ROLE_" + role.getName();
			
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
			
			authorities.add(simpleGrantedAuthority);
		}
		
		// 7. Traverse authNameList to save permission information
		for (String authName : authNameList) {
			
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);
			
			authorities.add(simpleGrantedAuthority);
		}
		
		// 8. Encapsulate the SecurityAdmin object
		SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);
		
		return securityAdmin;
	}

}
