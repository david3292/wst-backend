package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class CotizacionClienteNoEncontradoException extends VentasException {

	@Override
	public String getMessage() {
		return "Cliente no encontrado";
	}
}
