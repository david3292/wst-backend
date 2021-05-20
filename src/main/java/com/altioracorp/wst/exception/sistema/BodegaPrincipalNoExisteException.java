package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class BodegaPrincipalNoExisteException extends SistemaException{

	public BodegaPrincipalNoExisteException() {
	}

	@Override
	public String getMessage() {
		return String.format("No se encontro ninguna Bodega Principal asociada.");
	}

}
