package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class PuntoVentaNoExisteException extends SistemaException {
	
	@Override
	public String getMessage() {
		return String.format("No existe el punto de penta");
	}
}
