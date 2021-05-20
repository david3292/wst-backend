package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class ArticuloPendienteRecepcionException extends VentasException{

	@Override
	public String getMessage() {		
		return "El artículo esta pendiente de recepción";
	}

}
