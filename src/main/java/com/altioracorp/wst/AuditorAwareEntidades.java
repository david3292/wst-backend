package com.altioracorp.wst;

import static com.altioracorp.wst.util.UtilidadesCadena.truncar;
import static com.altioracorp.wst.util.UtilidadesSeguridad.esUsuarioAnonimo;
import static com.altioracorp.wst.util.UtilidadesSeguridad.esUsuarioIdentificado;
import static com.altioracorp.wst.util.UtilidadesSeguridad.usuarioEnSesion;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareEntidades implements AuditorAware<String> {
	
	private static final int AUDITORIA_LONGITUD_USUARIO_MAX = 100;
	private static final String AUDITORIA_USUARIO_DESCONOCIDO = "desconocido";
	private static final String AUDITORIA_USUARIO_ANONIMO = "anonimo";
	
	@Override
	public Optional<String> getCurrentAuditor() {
		
		if (esUsuarioIdentificado()) {
			return Optional.of(usuarioDeSesion());
		} else if (esUsuarioAnonimo()) {
			return Optional.of(AUDITORIA_USUARIO_ANONIMO);
		} else {
			return Optional.of(AUDITORIA_USUARIO_DESCONOCIDO);
		}
	}

	private final String usuarioDeSesion() {
		final String userDetailsNombreUsuario = usuarioEnSesion();
		final String usuario = String.format("%s", userDetailsNombreUsuario);
		return truncar(usuario, AUDITORIA_LONGITUD_USUARIO_MAX);
	}
}
