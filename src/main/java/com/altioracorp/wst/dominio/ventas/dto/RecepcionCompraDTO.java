package com.altioracorp.wst.dominio.ventas.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.altioracorp.wst.dominio.compras.RecepcionCompraEstado;

public class RecepcionCompraDTO {

	private long id;
	
	private String numeroRecepcion;
	
	private String ordenCompraNumero;
	
	@Enumerated(EnumType.STRING)
	private RecepcionCompraEstado estado;
	
	private String mensajeError;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumeroRecepcion() {
		return numeroRecepcion;
	}

	public void setNumeroRecepcion(String numeroRecepcion) {
		this.numeroRecepcion = numeroRecepcion;
	}

	public String getOrdenCompraNumero() {
		return ordenCompraNumero;
	}

	public void setOrdenCompraNumero(String ordenCompraNumero) {
		this.ordenCompraNumero = ordenCompraNumero;
	}

	public RecepcionCompraEstado getEstado() {
		return estado;
	}

	public void setEstado(RecepcionCompraEstado estado) {
		this.estado = estado;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	
}
