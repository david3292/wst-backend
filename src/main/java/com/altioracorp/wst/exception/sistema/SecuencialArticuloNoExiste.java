package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class SecuencialArticuloNoExiste extends SistemaException{

	@Override
	public String getMessage() {		
		return "No existe secuencial para la clase seleccionada";
	}

}
