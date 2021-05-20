package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class ProcesoFacturaCerrarExisteReservaPendienteException extends VentasException {

	private String numeroCotizacion;
	
	public ProcesoFacturaCerrarExisteReservaPendienteException(String numeroCotizacion) {
		this.numeroCotizacion = numeroCotizacion;
	}

	@Override
	public String getMessage() {
		return String.format("No se puede cerrar la Cotización %s, existen reservas pendientes", numeroCotizacion);
	}

}
