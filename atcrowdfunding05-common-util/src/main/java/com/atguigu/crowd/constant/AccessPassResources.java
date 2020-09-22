package com.atguigu.crowd.constant;

import java.util.HashSet;
import java.util.Set;

public class AccessPassResources {
	
	public static final Set<String> PASS_RES_SET = new HashSet<>();
	
	static {
		PASS_RES_SET.add("/");
		PASS_RES_SET.add("/auth/member/to/reg/page");
		PASS_RES_SET.add("/auth/member/to/login/page");
		PASS_RES_SET.add("/auth/member/logout");
		PASS_RES_SET.add("/auth/member/do/login");
		PASS_RES_SET.add("/auth/do/member/register");
		PASS_RES_SET.add("/auth/member/send/short/message.json");
	}
	
	public static final Set<String> STATIC_RES_SET = new HashSet<>();
	
	static {
		STATIC_RES_SET.add("bootstrap");
		STATIC_RES_SET.add("css");
		STATIC_RES_SET.add("fonts");
		STATIC_RES_SET.add("img");
		STATIC_RES_SET.add("jquery");
		STATIC_RES_SET.add("layer");
		STATIC_RES_SET.add("script");
		STATIC_RES_SET.add("ztree");
	}

	/**
	 * Used to determine whether a ServletPath value corresponds to a static resource
	 * @param servletPath
	 * @return
	 * 		true：is a static resource
	 * 		false：not a static resource
	 */
	public static boolean judgeCurrentServletPathWetherStaticResource(String servletPath) {
		
		// 1. Exclude the invalid string
		if(servletPath == null || servletPath.length() == 0) {
			throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
		}
		
		// 2. Split ServletPath string according to "/"
		String[] split = servletPath.split("/");
		
		// 3. Considering that an empty string is the first element of the array after splitting the left side of the first slash, so you need to use subscript 1 to take the second element
		String firstLevelPath = split[1];
		
		// 4. Determine if it is in the collection
		return STATIC_RES_SET.contains(firstLevelPath);
	}
	
//	public static void main(String[] args) {
//		String servletPath = "/";
//		boolean result = judgeCurrentServletPathWetherStaticResource(servletPath);
//		System.out.println(result);
//	}

}
