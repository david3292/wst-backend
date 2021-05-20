package com.altioracorp.wst.util.validacionNumeroIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionPatronInvalidoException extends ValidacionNumeroIdentificacionException {

	@Override
	public String getMessage() {
		return "El patrón del número no es el esperado";
	}
}
