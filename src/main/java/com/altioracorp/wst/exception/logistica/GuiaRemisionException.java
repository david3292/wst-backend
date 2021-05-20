package com.altioracorp.wst.exception.logistica;

import com.altioracorp.wst.exception.ventas.VentasException;

@SuppressWarnings("serial")
public class GuiaRemisionException extends VentasException{

	private String mensajeError;
	
	public GuiaRemisionException(String mensajeError) {
		super();
		this.mensajeError = mensajeError;
	}
	
	@Override
	public String getMessage() {
		return this.mensajeError;
	}

}
