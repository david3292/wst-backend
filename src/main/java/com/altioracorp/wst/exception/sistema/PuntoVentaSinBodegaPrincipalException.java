package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class PuntoVentaSinBodegaPrincipalException extends SistemaException {

	String puntoVenta;

	public PuntoVentaSinBodegaPrincipalException(String puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	@Override
	public String getMessage() {
		return String.format("El punto de venta %s no tiene bodega principal.", puntoVenta);
	}

}
