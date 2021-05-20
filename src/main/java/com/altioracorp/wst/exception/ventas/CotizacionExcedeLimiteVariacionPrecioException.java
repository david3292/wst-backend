package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class CotizacionExcedeLimiteVariacionPrecioException extends VentasException {

	private String valor;
	
	public CotizacionExcedeLimiteVariacionPrecioException(String valor) {
		super();
		this.valor = valor;
	}

	@Override
	public String getMessage() {
		return String.format("Ha superado el máximo porcentaje de variación de precio configurado: %s", valor+" %");
	}

}
