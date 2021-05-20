package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class BodegaYaExisteException extends SistemaException{

	private String codigo;
	
	public BodegaYaExisteException(String codigo) {
		
	}
	
	@Override
	public String getMessage() {
		return String.format("Bodega con código %s%, ya existe", this.codigo);
	}

}
