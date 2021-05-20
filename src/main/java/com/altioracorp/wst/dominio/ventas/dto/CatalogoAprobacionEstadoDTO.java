package com.altioracorp.wst.dominio.ventas.dto;

public class CatalogoAprobacionEstadoDTO {

	private String texto;
	
	private String valor;

	public CatalogoAprobacionEstadoDTO(String texto, String valor) {
		super();
		this.texto = texto;
		this.valor = valor;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
}
