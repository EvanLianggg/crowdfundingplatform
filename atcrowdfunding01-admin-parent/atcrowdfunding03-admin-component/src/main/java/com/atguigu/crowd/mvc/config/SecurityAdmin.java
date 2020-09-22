package com.atguigu.crowd.mvc.config;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.atguigu.crowd.entity.Admin;

/**
 * Considering that the User object only contains the account and password, in order to be able to obtain the original Admin object, this class is specially created to extend the User class
 * @author Lenovo
 *
 */
public class SecurityAdmin extends User {
	
	private static final long serialVersionUID = 1L;
	
	// The original Admin object contains all the attributes of the Admin object
	private Admin originalAdmin;
	
	public SecurityAdmin(
		    // Pass in the original Admin object
			Admin originalAdmin, 
			
				// Create a collection of roles and permission information
			List<GrantedAuthority> authorities) {
		
				// call the parent class constructor
		super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
		
		// Assign value to this.originalAdmin of this class
		this.originalAdmin = originalAdmin;
		
		// Erase the password in the original Admin object

		this.originalAdmin.setUserPswd(null);
		
	}
	
	// The getXxx() method for obtaining the original Admin object provided externally
	public Admin getOriginalAdmin() {
		return originalAdmin;
	}

}
