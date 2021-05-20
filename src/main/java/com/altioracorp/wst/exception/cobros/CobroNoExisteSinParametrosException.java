package com.altioracorp.wst.exception.cobros;

@SuppressWarnings("serial")
public class CobroNoExisteSinParametrosException extends CobrosException {

	@Override
	public String getMessage() {
		return String.format("Cobro no encontrado.");
	}

}
