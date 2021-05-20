package com.altioracorp.wst.exception.logistica;

import com.altioracorp.wst.exception.ventas.VentasException;

@SuppressWarnings("serial")
public class TransferenciaCompartimientosIncompletosException extends VentasException{

	private String mensaje;
	
	public TransferenciaCompartimientosIncompletosException(String mensaje) {
		super();
		this.mensaje = mensaje;
	}

	@Override
	public String getMessage() {
		return this.mensaje;
	}

}
