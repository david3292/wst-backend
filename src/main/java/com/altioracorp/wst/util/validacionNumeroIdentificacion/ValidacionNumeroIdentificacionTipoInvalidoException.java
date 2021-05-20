package com.altioracorp.wst.util.validacionNumeroIdentificacion;

import com.altioracorp.wst.dominio.ventas.TipoIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionTipoInvalidoException extends ValidacionNumeroIdentificacionException {

	private TipoIdentificacion tipo;

	public ValidacionNumeroIdentificacionTipoInvalidoException(TipoIdentificacion tipo) {
		this.tipo = tipo;		
	}
	
	@Override
	public String getMessage() {
		return "El tipo de documento no se puede validar: " + tipo;
	}
}
