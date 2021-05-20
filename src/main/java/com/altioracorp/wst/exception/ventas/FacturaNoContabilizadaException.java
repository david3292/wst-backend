package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class FacturaNoContabilizadaException extends VentasException {

	@Override
	public String getMessage() {
		return String.format("Factura no contabilizada ");
	}

}
