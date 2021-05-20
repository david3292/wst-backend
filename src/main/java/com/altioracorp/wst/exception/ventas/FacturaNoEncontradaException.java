package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class FacturaNoEncontradaException extends VentasException {

	private String numero;

	public FacturaNoEncontradaException(String numero) {
		super();
		this.numero = numero;
	}

	@Override
	public String getMessage() {
		return String.format("No se encuentra la factura N°: %s", numero);
	}

}
