package com.atguigu.crowd.mvc.handler;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;

@Controller
public class AdminHandler {
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping("/admin/update.html")
	public String update(Admin admin, @RequestParam("pageNum") Integer pageNum, @RequestParam("keyword") String keyword) {
		
		adminService.update(admin);
		
		return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
	}
	
	@RequestMapping("/admin/to/edit/page.html")
	public String toEditPage(
				@RequestParam("adminId") Integer adminId,
				ModelMap modelMap
			) {
		
				// 1. Query the Admin object based on adminId
		Admin admin = adminService.getAdminById(adminId);
		
		// 2. Save the Admin object into the model
		modelMap.addAttribute("admin", admin);
		
		return "admin-edit";
	}
	
	@PreAuthorize("hasAuthority('user:save')")
	@RequestMapping("/admin/save.html")
	public String save(Admin admin) {
		
		adminService.saveAdmin(admin);
		
		return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
	}
	
	@RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
	public String remove(
				@PathVariable("adminId") Integer adminId,
				@PathVariable("pageNum") Integer pageNum,
				@PathVariable("keyword") String keyword
			) {
		
				// execute delete
		adminService.remove(adminId);
		
		// Try solution 1: Forwarding directly to admin-page.jsp will fail to display paging data
		// return "admin-page";
		
		// Try solution 2: forward to the /admin/get/page.html address, once the page is refreshed, it will repeat the deletion and waste performance
		// return "forward:/admin/get/page.html";
		
		// Try solution 3: Redirect to /admin/get/page.html address
		// At the same time, in order to maintain the original page and query keywords, add pageNum and keyword two request parameters
		return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
	}
	
	@RequestMapping("/admin/get/page.html")
	public String getPageInfo(
				
		// Use the defaultValue attribute annotated by @RequestParam to specify the default value, and use the default value when the request does not carry the corresponding parameter
		// The keyword default value uses an empty string, and the SQL statement cooperates to achieve two cases of adaptation
				@RequestParam(value="keyword", defaultValue="") String keyword,
				
				// pageNum default value uses 1
				@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
				
				// // The default value of pageSize is 5

				@RequestParam(value="pageSize", defaultValue="5") Integer pageSize,
				
				ModelMap modelMap
			
			) {
		
		// Call the Service method to get the PageInfo object

		PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
		
		// Save the PageInfo object into the model
		modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
		
		return "admin-page";
	}
	
	@RequestMapping("/admin/do/logout.html")
	public String doLogout(HttpSession session) {
		
		// Force Session to fail
		session.invalidate();
		
		return "redirect:/admin/to/login/page.html";
	}
	
	@RequestMapping("/admin/do/login.html")
	public String doLogin(
				@RequestParam("loginAcct") String loginAcct,
				@RequestParam("userPswd") String userPswd,
				HttpSession session
			) {
		
				// Call the Service method to perform login check
				// If this method can return the admin object, the login is successful, if the account and 
		Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
		
		// Save the admin object returned after successful login into the Session domain
		session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
		
		return "redirect:/admin/to/main/page.html";
	}
	
	@ResponseBody
	@PostAuthorize("returnObject.data.loginAcct == principal.username")
	@RequestMapping("/admin/test/post/filter.json")
	public ResultEntity<Admin> getAdminById() {
		
		Admin admin = new Admin();
		
		admin.setLoginAcct("adminOperator");
		
		return ResultEntity.successWithData(admin);
	}
	
	@PreFilter(value="filterObject%2==0")
	@ResponseBody
	@RequestMapping("/admin/test/pre/filter")
	public ResultEntity<List<Integer>> saveList(@RequestBody List<Integer> valueList) {
		return ResultEntity.successWithData(valueList);
	}

}
