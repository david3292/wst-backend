package com.altioracorp.wst;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.altioracorp.wst.controlador.sistema.RolUsuario;

@SuppressWarnings("serial")
public class UserDetailsSuperadmin extends UserDetailsBase implements UserDetails {
	
	public static final String USUARIO = "superadmin";
	
	private String contrasena;

	public UserDetailsSuperadmin(String contrasena) {
		this.contrasena = contrasena;
	}

	@Override
	public String getAuditoriaNombre() {
		return "Superadmin";
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(RolUsuario.SUPERADMIN.name()));
	}

	@Override
	public String getPassword() {
		return contrasena;
	}

	@Override
	public String getUsername() {
		return USUARIO;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getUsuarioNombre() {
		return "Superadmin";
	}
}
