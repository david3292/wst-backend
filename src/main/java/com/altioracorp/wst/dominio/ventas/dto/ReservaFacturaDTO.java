package com.altioracorp.wst.dominio.ventas.dto;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.TipoReserva;

public class ReservaFacturaDTO {

	private Long idReserva;

	private String numeroReserva;
	
	private DocumentoEstado estado;
	
	private TipoReserva tipoReserva;

	
	public ReservaFacturaDTO() {
	}

	public ReservaFacturaDTO(Long idReserva, String numeroReserva, DocumentoEstado estado, TipoReserva tipoReserva) {
		super();
		this.idReserva = idReserva;
		this.numeroReserva = numeroReserva;
		this.estado = estado;
		this.tipoReserva = tipoReserva;
	}

	public Long getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(Long idReserva) {
		this.idReserva = idReserva;
	}

	public String getNumeroReserva() {
		return numeroReserva;
	}

	public void setNumeroReserva(String numeroReserva) {
		this.numeroReserva = numeroReserva;
	}

	public DocumentoEstado getEstado() {
		return estado;
	}

	public void setEstado(DocumentoEstado estado) {
		this.estado = estado;
	}

	public TipoReserva getTipoReserva() {
		return tipoReserva;
	}

	public void setTipoReserva(TipoReserva tipoReserva) {
		this.tipoReserva = tipoReserva;
	}
	
	
}
