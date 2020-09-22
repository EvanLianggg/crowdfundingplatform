package com.atguigu.crowd.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.AccessForbiddenException;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
				// 1. Get the Session object through the request object
		HttpSession session = request.getSession();
		
		// 2. Try to get the Admin object from the Session domain
		Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
		
		// 3. Determine whether the admin object is empty
		if(admin == null) {
			
			// 4. Throw an exception
			throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
			
		}
		
		// 5. If the Admin object is not null, return true and let go
		return true;
	}

}
