package com.altioracorp.wst;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.altioracorp.wst.dominio.ldap.UserDetailsVO;
import com.altioracorp.wst.dominio.ldap.UsuarioLdap;
import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.repositorio.sistema.IConfiguracionUsuarioPerfilRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.servicioImpl.ldap.ILdapServicio;

@Component
public class AuthenticationProviderLDAP implements AuthenticationProvider {

	private ILdapServicio servicioLdap;
	private IUsuarioRepositorio usuarioRepositorio;
	private IConfiguracionUsuarioPerfilRepositorio confignUsuarioPerfilRepositorio;
	
	public AuthenticationProviderLDAP(ILdapServicio servicioLdap, IUsuarioRepositorio usuarioRepositorio, IConfiguracionUsuarioPerfilRepositorio confignUsuarioPerfilRepositorio) {
		this.servicioLdap = servicioLdap;
		this.usuarioRepositorio = usuarioRepositorio;
		this.confignUsuarioPerfilRepositorio = confignUsuarioPerfilRepositorio;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String usuario = authentication.getName();
		String contrasena = authentication.getCredentials().toString();
		final Optional<UsuarioLdap> usuarioLdap = servicioLdap.findByUserName(usuario);
		
		if (usuarioLdap.isPresent() && servicioLdap.authenticate(usuario, contrasena)) {
			
			final UsuarioLdap usuarioLdapEncontrado = usuarioLdap.get();
			
			final Optional<Usuario> usuarioWst = usuarioRepositorio.findByNombreUsuarioAndActivoTrue(usuarioLdapEncontrado.getUserId());
			
			if (usuarioWst.isEmpty()) {
				return null;
			}
			
			List<ConfiguracionUsuarioPerfil> configuraciones = confignUsuarioPerfilRepositorio.findByUsuarioPerfil_Usuario(usuarioWst.get());
			List<PuntoVenta> puntosVentaAsocioados = new ArrayList<>();
			List<String> perfiles = new ArrayList<>();
			configuraciones.forEach(x ->{
				puntosVentaAsocioados.add(x.getPuntoVenta());
				perfiles.add(x.getUsuarioPerfil().getPerfil().getNombre().toString());
			});
			
			UserDetailsVO userDetailsVO = new UserDetailsVO();
			userDetailsVO.setUsuario(usuarioWst.get());
			userDetailsVO.setPuntosVenta(puntosVentaAsocioados);
			userDetailsVO.setPerfiles(perfiles);
			return new UserDetailsUsuarioLDAP(userDetailsVO, contrasena);
		}else {
			return null;
		}		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
