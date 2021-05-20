package com.altioracorp.wst.dominio.logistica.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.util.LocalDateDeserialize;
import com.altioracorp.wst.util.LocalDateSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ReposicionDTO {

	private long idReposicion;
	
	private String bodegaOrigen;

	private String bodegaDestino;

	private String numero;

	private DocumentoEstado estado;

	@JsonSerialize(using = LocalDateSerialize.class)
	@JsonDeserialize(using = LocalDateDeserialize.class)
	private LocalDate fecha = LocalDate.now();

	private List<ReposicionDetalleDTO> detalle = new ArrayList<ReposicionDetalleDTO>();

	public ReposicionDTO() {
	}

	public ReposicionDTO(long id, String bodegaOrigen, String bodegaDestino, String numero, DocumentoEstado estado,
			LocalDate fecha) {
		super();
		this.idReposicion = id;
		this.bodegaOrigen = bodegaOrigen;
		this.bodegaDestino = bodegaDestino;
		this.numero = numero;
		this.estado = estado;
		this.fecha = fecha;
	}

	public long getIdReposicion() {
		return idReposicion;
	}

	public void setIdReposicion(long idReposicion) {
		this.idReposicion = idReposicion;
	}

	public void agregarLineaDetalle(ReposicionDetalleDTO dto) {
		this.detalle.add(dto);
	}

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

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public List<ReposicionDetalleDTO> getDetalle() {
		return detalle;
	}

	public void setDetalle(List<ReposicionDetalleDTO> detalle) {
		this.detalle = detalle;
	}

}
