package com.altioracorp.wst.util.validacionNumeroIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionProvinciaInvalidaException extends ValidacionNumeroIdentificacionException {

	private short provincia;
	
	public ValidacionNumeroIdentificacionProvinciaInvalidaException(short provincia) {
		this.provincia = provincia;
	}

	@Override
	public String getMessage() {
		return "La provincia es inválida: " + provincia;
	}
}
