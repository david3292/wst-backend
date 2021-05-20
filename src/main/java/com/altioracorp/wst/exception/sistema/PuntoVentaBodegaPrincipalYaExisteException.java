package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class PuntoVentaBodegaPrincipalYaExisteException extends SistemaException {

	String bodega;
	
	public PuntoVentaBodegaPrincipalYaExisteException(String bodega) {
		this.bodega = bodega;
	}

	@Override
	public String getMessage() {
		return String.format("La Bodega %s ya se encuentra como principal.", bodega);
	}

}
