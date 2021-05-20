package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class FacturaCantidadExcedeASaldoCotizacionException extends VentasException {

	private String codigoArticulo;
	
	public FacturaCantidadExcedeASaldoCotizacionException(String codigoArticulo) {
		super();
		this.codigoArticulo = codigoArticulo;
	}

	@Override
	public String getMessage() {
		return String.format("La cantidad seleccionad de %s, excede al saldo de la cotización.", codigoArticulo);
	}

}
