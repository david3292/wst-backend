package com.altioracorp.wst;


import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public abstract class UserDetailsBase implements UserDetails {
	
	public abstract String getAuditoriaNombre();

	public final String getAuditoriaUsuario() {
		return getUsername();
	}

	@Override
	public final boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public final boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public final boolean isCredentialsNonExpired() {
		return true;
	}
}
