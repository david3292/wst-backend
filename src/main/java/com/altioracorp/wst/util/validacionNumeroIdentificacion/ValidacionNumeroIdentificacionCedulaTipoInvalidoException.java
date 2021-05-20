package com.altioracorp.wst.util.validacionNumeroIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionCedulaTipoInvalidoException extends ValidacionNumeroIdentificacionException {

	private short tipo;

	public ValidacionNumeroIdentificacionCedulaTipoInvalidoException(short tipo) {
		this.tipo = tipo;
	}

	@Override
	public String getMessage() {
		return "El tipo es inválido: " + tipo;
	}
}
