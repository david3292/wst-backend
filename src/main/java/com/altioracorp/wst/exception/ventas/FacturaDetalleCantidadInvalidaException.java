package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class FacturaDetalleCantidadInvalidaException extends VentasException {

	@Override
	public String getMessage() {
		return String.format("En una de las bodegas la cantidad es inválida, no puede ser 0");
	}

}
