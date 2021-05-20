package com.altioracorp.wst.dominio.ventas;

public enum Entrega {

	CLIENTE("Cliente Retira"), 
	DESPACHO("Despacho");

	private String descripcion;

	private Entrega(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

}
