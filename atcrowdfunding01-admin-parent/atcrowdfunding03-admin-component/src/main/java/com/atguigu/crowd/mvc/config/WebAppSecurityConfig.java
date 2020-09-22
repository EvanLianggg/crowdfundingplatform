package com.atguigu.crowd.mvc.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.atguigu.crowd.constant.CrowdConstant;

// Indicates that the current class is a configuration class
@Configuration

// Enable permission control function in Web environment
@EnableWebSecurity

// Enable the global method permission control function, and set prePostEnabled = true. Ensure that @PreAuthority, @PostAuthority, @PreFilter, and @PostFilter take effect
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	/*
	 * 
	It is stated here that it cannot be assembled in XxxService
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}*/
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		
		// Temporarily use memory version login mode test code
		// builder.inMemoryAuthentication().withUser("tom").password("123123").roles("ADMIN");
		
		// Use database-based authentication in official functions

		builder
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
		
	}
	
	@Override
	protected void configure(HttpSecurity security) throws Exception {
		
		security
			.authorizeRequests() // authorize the request
			.antMatchers("/admin/to/login/page.html") // set up for login page
			.permitAll() // unconditional access
			.antMatchers("/bootstrap/**") // set for static resources, unconditional access
			.permitAll() // set for static resources, unconditional access
			.antMatchers("/crowd/**") // set for static resources, unconditional access
			.permitAll() // set for static resources, unconditional access
			.antMatchers("/css/**") // set for static resources, unconditional access
			.permitAll() // set for static resources, unconditional access
			.antMatchers("/fonts/**") // set for static resources, unconditional access
			.permitAll() // set for static resources, unconditional access
			.antMatchers("/img/**") // set for static resources, unconditional access
			.permitAll() // set for static resources, unconditional access
			.antMatchers("/jquery/**") // set for static resources, unconditional access
			.permitAll() // set for static resources, unconditional access
			.antMatchers("/layer/**") // set for static resources, unconditional access
			.permitAll() // set for static resources, unconditional access
			.antMatchers("/script/**") // Set up for static resources, unconditional access
			.permitAll() // set for static resources, unconditional access
			.antMatchers("/ztree/**") // set for static resources, unconditional access
			.permitAll()
			.antMatchers("/admin/get/page.html") // Set access control for Admin data displayed on page
			// .hasRole("Manager") // requires a manager role
			.access("hasRole('manager') OR hasAuthority('user:get')") // requires one of the "manager" role and "user:get" permission
			.anyRequest() // other arbitrary requests
			.authenticated() // access after authentication
			.and()
			.exceptionHandling()
			.accessDeniedHandler(new AccessDeniedHandler() {
			
				
				@Override
				public void handle(HttpServletRequest request, HttpServletResponse response,
						AccessDeniedException accessDeniedException) throws IOException, ServletException {
					request.setAttribute("exception", new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
					request.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(request, response);
				}
			})
				.and()
				.csrf() // Anti-cross-site request forgery function
				.disable() // disable
				.formLogin() // Turn on form login function
				.loginPage("/admin/to/login/page.html") // Specify the login page
				.loginProcessingUrl("/security/do/login.html") // Specify the address for processing login requests
				.defaultSuccessUrl("/admin/to/main/page.html") // Specify the address to go to after successful login
				.usernameParameter("loginAcct") // The request parameter name of the account
				.passwordParameter("userPswd") // Password request parameter name
				.and()
				.logout() // enable logout function
				.logoutUrl("/seucrity/do/logout.html") // Specify the logout address
				.logoutSuccessUrl("/admin/to/login/page.html") // Specify the address to go to after successful logout
				;
		
	}

}
