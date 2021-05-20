package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class FacturaAnularEstadoException extends VentasException {

	private String numeroCotizacion;
	
	private String estado;
	
	public FacturaAnularEstadoException(String numeroCotizacion, String estado) {
		this.numeroCotizacion = numeroCotizacion;
		this.estado = estado;
	}

	@Override
	public String getMessage() {
		return String.format("No se puede anular Factura con estado %s  de Cotización %s ", estado, numeroCotizacion);
	}

}
