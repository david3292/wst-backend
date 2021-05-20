package com.altioracorp.wst.dominio.sistema;

public enum TipoPago {

	CONTADO("CONTADO"),
	CREDITO("CRÉDITO"),
	CREDITO_SOPORTE("CRÉDITO SOPORTE");
	
	private String descripcion;
	
	private TipoPago(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
}
