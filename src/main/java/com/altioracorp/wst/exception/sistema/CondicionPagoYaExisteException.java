package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class CondicionPagoYaExisteException  extends SistemaException{

	private String termino;
	
	public CondicionPagoYaExisteException(String termino) {
		this.termino = termino;
	}
	
	@Override
	public String getMessage() {		
		return String.format("Condición de Pago %s ya existe", termino);
	}

}
