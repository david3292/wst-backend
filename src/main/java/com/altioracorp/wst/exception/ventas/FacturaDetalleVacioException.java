package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class FacturaDetalleVacioException extends VentasException {

	@Override
	public String getMessage() {
		return String.format("Error al finalizar Factura, no existe detalle ");
	}

}
