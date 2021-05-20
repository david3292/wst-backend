package com.altioracorp.wst.dominio.cobros;

public class CobroFormaPagoDTO {

	private CobroFormaPagoEstado estado;

	private String numero;

	public CobroFormaPagoEstado getEstado() {
		return estado;
	}

	public String getNumero() {
		return numero;
	}

	public void setEstado(CobroFormaPagoEstado estado) {
		this.estado = estado;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

}
