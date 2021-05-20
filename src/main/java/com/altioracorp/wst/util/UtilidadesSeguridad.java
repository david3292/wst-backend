package com.altioracorp.wst.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.altioracorp.wst.UserDetailsUsuarioLDAP;

public class UtilidadesSeguridad {

	private static final Authentication authentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	private final static boolean authenticationCorrespondeAUsuarioAnonimo(Authentication authentication) {
		return authentication.getName().equals("anonymousUser");
	}
	
	public static final boolean esUsuarioAnonimo() {
		final Authentication authentication = authentication();
		return (authentication != null) && (authenticationCorrespondeAUsuarioAnonimo(authentication));
	}
	
	public static final boolean esUsuarioIdentificado() {
		final Authentication authentication = authentication();
		return (authentication != null) && (!authenticationCorrespondeAUsuarioAnonimo(authentication));
	}
	
	public static final String nombreUsuarioEnSesion() {
		return authentication().getName();
	}
	
	public static final String usuarioEnSesion() {
		return authentication() == null ? "desconocido" : (String) authentication().getPrincipal();
	}

	public static final UserDetailsUsuarioLDAP usuarioDetalleEnSesion() {	
		return (UserDetailsUsuarioLDAP)authentication();
	}
	
	private UtilidadesSeguridad() {}
	
}
