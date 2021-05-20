package com.altioracorp.wst;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Qualifier(UserDetailsServices.SUPERADMIN)
public class UserDetailsServiceSuperadmin implements UserDetailsServiceBase<UserDetailsSuperadmin> {
	
	private Environment environment;

	@Autowired
	public UserDetailsServiceSuperadmin(
			Environment environment) {
		
		this.environment = environment;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username.equals(UserDetailsSuperadmin.USUARIO)) {			
			return new UserDetailsSuperadmin(environment.getRequiredProperty("superadmin.contrasena"));
		} else {
			throw new UsernameNotFoundException(username);
		}
	}
}
