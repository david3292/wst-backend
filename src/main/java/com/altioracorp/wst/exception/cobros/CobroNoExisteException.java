package com.altioracorp.wst.exception.cobros;

@SuppressWarnings("serial")
public class CobroNoExisteException extends CobrosException {

	private String numero;	
	
	public CobroNoExisteException(String numero) {
		this.numero = numero;
	}

	@Override
	public String getMessage() {
		return String.format("Cobro %s no encontrado.", numero);
	}

}
