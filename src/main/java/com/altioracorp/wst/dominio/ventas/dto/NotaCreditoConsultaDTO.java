package com.altioracorp.wst.dominio.ventas.dto;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;

public class NotaCreditoConsultaDTO {

	private Long id;

	private String numero;

	private DocumentoEstado estado;

	private String error;

	public NotaCreditoConsultaDTO(Long id, String numero, DocumentoEstado estado, String error) {
		super();
		this.id = id;
		this.numero = numero;
		this.estado = estado;
		this.error = error;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public DocumentoEstado getEstado() {
		return estado;
	}

	public void setEstado(DocumentoEstado estado) {
		this.estado = estado;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
