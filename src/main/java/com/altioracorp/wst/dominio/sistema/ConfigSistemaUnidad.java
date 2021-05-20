package com.altioracorp.wst.dominio.sistema;

public enum ConfigSistemaUnidad {

	HORAS("Horas"),
	DIAS("Días"),
	PORCENTAJE("Porcentaje");
	
	private String descripcion;

	private ConfigSistemaUnidad(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	
	
}
