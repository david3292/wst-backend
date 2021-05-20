package com.altioracorp.wst.exception.logistica;


@SuppressWarnings("serial")
public class GuiaRemisionNoEncontrada extends LogisticaException{

	@Override
	public String getMessage() {
		return "Guía de remisión no encontrada";
	}

}
