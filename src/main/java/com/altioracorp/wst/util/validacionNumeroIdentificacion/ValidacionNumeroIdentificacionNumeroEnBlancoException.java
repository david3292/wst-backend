package com.altioracorp.wst.util.validacionNumeroIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionNumeroEnBlancoException extends ValidacionNumeroIdentificacionException {

	@Override
	public String getMessage() {
		return "El número proporcionado es una cadena en blanco";
	}
}
