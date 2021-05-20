package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class CotizacionNumeroYaExisteException extends VentasException {

	private String numero;

	public CotizacionNumeroYaExisteException(String numero) {
		this.numero = numero;
	}

	@Override
	public String getMessage() {
		return String.format("Cotización con Número %s ya existe.", this.numero);
	}
}
