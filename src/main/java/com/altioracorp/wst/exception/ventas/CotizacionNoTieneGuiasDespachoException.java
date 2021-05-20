package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class CotizacionNoTieneGuiasDespachoException extends VentasException{

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "No se encontró ninguna Guía de Despacho";
	}
	

}
