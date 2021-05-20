package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class OrdenCompraException extends VentasException{

	private String mensaje;
	
	public OrdenCompraException(String mensaje) {
		this.mensaje = mensaje;
	}
	
	@Override
	public String getMessage() {
		return this.mensaje;
	}

}
