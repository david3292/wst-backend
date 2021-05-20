package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class GuiaDespachoNoExisteException extends VentasException {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Guía de Despacho no encontrada";
	}

}
