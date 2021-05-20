package com.altioracorp.wst.dominio.ventas;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.sistema.TipoDocumento;

@SuppressWarnings("serial")
@Entity
public class DocumentoTransferencia extends DocumentoBase {

	private long documentoPadreId;

	@NotNull
	private String bodegaOrigen;

	@NotNull
	private String bodegaDestino;

	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoTransferencia tipoTransferencia;

	@Transient
	private String entregaMercaderia;

	@Transient
	private String descBodegaOrigen;
	
	@Transient
	private String descBodegaDestino;
	
	@Transient
	private String direccionBodegaOrigen;
	
	@Transient
	private String direccionBodegaDestino;
	
	public DocumentoTransferencia() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public DocumentoTransferencia(@NotNull String bodegaOrigen, @NotNull String bodegaDestino) {
		super(LocalDateTime.now(), DocumentoEstado.NUEVO, TipoDocumento.TRANSFERENCIA);
		this.bodegaOrigen = bodegaOrigen;
		this.bodegaDestino = bodegaDestino;
		this.tipoTransferencia = TipoTransferencia.REPOSICION;
	}
	
	public DocumentoTransferencia(@NotNull String bodegaOrigen, @NotNull String bodegaDestino,
			DocumentoBase documetoPadre) {
		super(LocalDateTime.now(), documetoPadre.getCotizacion(), DocumentoEstado.NUEVO, TipoDocumento.TRANSFERENCIA);
		this.documentoPadreId = documetoPadre.getId();
		this.bodegaOrigen = bodegaOrigen;
		this.bodegaDestino = bodegaDestino;
		this.tipoTransferencia = TipoTransferencia.VENTA;
	}

	public String getBodegaDestino() {
		return bodegaDestino;
	}
	
	public String getBodegaOrigen() {
		return bodegaOrigen;
	}

	public String getDescBodegaDestino() {
		return descBodegaDestino;
	}

	public String getDescBodegaOrigen() {
		return descBodegaOrigen;
	}

	public String getDireccionBodegaDestino() {
		return direccionBodegaDestino;
	}

	public String getDireccionBodegaOrigen() {
		return direccionBodegaOrigen;
	}

	public long getDocumentoPadreId() {
		return documentoPadreId;
	}

	public String getEntregaMercaderia() {
		return entregaMercaderia;
	}

	public TipoTransferencia getTipoTransferencia() {
		return tipoTransferencia;
	}

	public void setBodegaDestino(String bodegaDestino) {
		this.bodegaDestino = bodegaDestino;
	}

	public void setBodegaOrigen(String bodegaOrigen) {
		this.bodegaOrigen = bodegaOrigen;
	}

	public void setDescBodegaDestino(String descBodegaDestino) {
		this.descBodegaDestino = descBodegaDestino;
	}

	public void setDescBodegaOrigen(String descBodegaOrigen) {
		this.descBodegaOrigen = descBodegaOrigen;
	}

	public void setDireccionBodegaDestino(String direccionBodegaDestino) {
		this.direccionBodegaDestino = direccionBodegaDestino;
	}

	public void setDireccionBodegaOrigen(String direccionBodegaOrigen) {
		this.direccionBodegaOrigen = direccionBodegaOrigen;
	}

	public void setDocumentoPadreId(long documentoPadreId) {
		this.documentoPadreId = documentoPadreId;
	}

	public void setEntregaMercaderia(String entregaMercaderia) {
		this.entregaMercaderia = entregaMercaderia;
	}

	public void setTipoTransferencia(TipoTransferencia tipoTransferencia) {
		this.tipoTransferencia = tipoTransferencia;
	}
	
}
