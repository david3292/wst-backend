package com.altioracorp.wst.exception.cobros;

@SuppressWarnings("serial")
public class CobroNoCumpleCondicionesException extends CobrosException {

	private String numeroCobro;

	public CobroNoCumpleCondicionesException(String numeroCobro) {
		this.numeroCobro = numeroCobro;
	}

	@Override
	public String getMessage() {
		return String.format("El cobro: %s no suma con el total de la deuda.", numeroCobro);
	}

}
