package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class FacturaBodegaPrincipalNoExisteException extends VentasException{
	
	private String puntoVenta;
	
	public FacturaBodegaPrincipalNoExisteException(String puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	@Override
	public String getMessage() {
		return String.format("No existe bodega Principal en PuntoVenta %s ", puntoVenta);
	}

}
