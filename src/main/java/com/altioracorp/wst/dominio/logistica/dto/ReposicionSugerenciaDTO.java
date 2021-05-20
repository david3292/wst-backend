package com.altioracorp.wst.dominio.logistica.dto;

public class ReposicionSugerenciaDTO {

	private String bodegaOrigen;
	
	private String bodegaDestino;	
	
	public ReposicionSugerenciaDTO() {	}
	
	public String getBodegaOrigen() {
		return bodegaOrigen;
	}
	public void setBodegaOrigen(String bodegaOrigen) {
		this.bodegaOrigen = bodegaOrigen;
	}
	public String getBodegaDestino() {
		return bodegaDestino;
	}
	public void setBodegaDestino(String bodegaDestino) {
		this.bodegaDestino = bodegaDestino;
	}
	
	
}
