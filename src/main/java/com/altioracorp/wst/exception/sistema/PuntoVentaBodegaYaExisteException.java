package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class PuntoVentaBodegaYaExisteException extends SistemaException {

	String bodega;
	
	public PuntoVentaBodegaYaExisteException(String bodega) {
		this.bodega = bodega;
	}

	@Override
	public String getMessage() {
		return String.format("La Bodega %s ya se encuentra asignada", bodega);
	}

}
