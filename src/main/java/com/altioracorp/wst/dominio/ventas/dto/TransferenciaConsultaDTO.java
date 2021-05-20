package com.altioracorp.wst.dominio.ventas.dto;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;

public class TransferenciaConsultaDTO {

	private Long id;

	private String numero;

	private DocumentoEstado estado;

	private String error;

	public TransferenciaConsultaDTO(Long id, String numero, DocumentoEstado estado, String error) {
		this.id = id;
		this.numero = numero;
		this.estado = estado;
		this.error = error;
	}

	public Long getId() {
		return id;
	}

	public String getNumero() {
		return numero;
	}

	public DocumentoEstado getEstado() {
		return estado;
	}

	public String getError() {
		return error;
	}

}
