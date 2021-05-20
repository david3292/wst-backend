package com.altioracorp.wst.exception.logistica;

@SuppressWarnings("serial")
public class TransferenciaNoEncontrada extends LogisticaException{

	@Override
	public String getMessage() {
		return "Transferencia no encontrada";
	}

}
