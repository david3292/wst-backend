package com.altioracorp.wst.controlador.sistema;

public class CatalogoDTO {
	
	private String texto;
	
	private String valor;

	public CatalogoDTO(String texto, String valor) {
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
