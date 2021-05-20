package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class CotizacionNoEncontrada extends VentasException {

	@Override
	public String getMessage() {
		return "Cotización No Encontrada";
	}
}
