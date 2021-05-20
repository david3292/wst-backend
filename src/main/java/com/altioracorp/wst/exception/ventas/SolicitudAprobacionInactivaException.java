package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class SolicitudAprobacionInactivaException extends VentasException {

	@Override
	public String getMessage() {
		return "Solicitud Aprobación ya no está activa";
	}
}
