package com.altioracorp.wst;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.altioracorp.wst.dominio.ldap.UserDetailsVO;

@SuppressWarnings("serial")
public class UserDetailsUsuarioLDAP extends UsernamePasswordAuthenticationToken implements UserDetails {
	
	private transient UserDetailsVO userDetailsVO;
	
	public UserDetailsUsuarioLDAP(UserDetailsVO userDetailsVO, String contrasena) {
		super(userDetailsVO.getUsuario().getNombreUsuario(), contrasena, new ArrayList<>());
		this.userDetailsVO = userDetailsVO;
	}

	public UserDetailsVO getUserDetailsVO() {
		return userDetailsVO;
	}

	public void setUserDetailsVO(UserDetailsVO userDetailsVO) {
		this.userDetailsVO = userDetailsVO;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();		
		this.userDetailsVO.getPerfiles().forEach(x -> {
			authorities.add(new SimpleGrantedAuthority(x));
		});
		
		return authorities;
	}

	public String getUsuarioNombre() {
		return this.userDetailsVO.getUsuario().getNombreUsuario();
	}

}
