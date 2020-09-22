package com.atguigu.crowd.mvc.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;

// @ControllerAdvice indicates that the current class is an annotation-based exception handler class
@ControllerAdvice
public class CrowdExceptionResolver {
	
	@ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
	public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(
			LoginAcctAlreadyInUseForUpdateException exception,
			HttpServletRequest request,
			HttpServletResponse response
			) throws IOException {
		
		String viewName = "system-error";
		
		return commonResolve(viewName, exception, request, response);
	}
	
	@ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
	public ModelAndView resolveLoginAcctAlreadyInUseException(
			LoginAcctAlreadyInUseException exception,
			HttpServletRequest request,
			HttpServletResponse response
			) throws IOException {
		
		String viewName = "admin-add";
		
		return commonResolve(viewName, exception, request, response);
	}
	
	@ExceptionHandler(value = LoginFailedException.class)
	public ModelAndView resolveLoginFailedException(
			LoginFailedException exception,
			HttpServletRequest request,
			HttpServletResponse response
			) throws IOException {
		
		String viewName = "admin-login";
		
		return commonResolve(viewName, exception, request, response);
	}
	
	@ExceptionHandler(value = Exception.class)
	public ModelAndView resolveException(
			Exception exception,
			HttpServletRequest request,
			HttpServletResponse response
			) throws IOException {
		
		String viewName = "system-error";
		
		return commonResolve(viewName, exception, request, response);
	}
	
// @ExceptionHandler associates a specific exception type with a method
	private ModelAndView commonResolve(
			
			// The page to go to after the exception handling is completed
			String viewName, 
			
			// The type of exception actually caught
			Exception exception, 
			
			// Current request object
			HttpServletRequest request, 
			
			// Current response object
			HttpServletResponse response) throws IOException {
		
		// 1.Determine the current request type
		boolean judgeResult = CrowdUtil.judgeRequestType(request);
		
		// 2.If it is an Ajax request
		if(judgeResult) {
			
			// 3.Create ResultEntity object
			ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
			
			// 4.Create Gson object
			Gson gson = new Gson();
			
			// 5.Convert ResultEntity object to JSON string
			String json = gson.toJson(resultEntity);
			
			// 6.Return the JSON string as the response body to the browser
			response.getWriter().write(json);
			
			// 7. Since the response has been returned through the native response object above, the ModelAndView object is not provided
			return null;
		}
		
		// 8.If it is not an Ajax request, create a ModelAndView object
		ModelAndView modelAndView = new ModelAndView();
		
		// 9.Store the Exception object in the model
		modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
		
		// 10.Set the corresponding view name
		modelAndView.setViewName(viewName);
		
		// 11.Return modelAndView object
		return modelAndView;
	}

}
