package com.altioracorp.wst.dominio.ventas;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;

@SuppressWarnings("serial")
@Entity
public class DocumentoTransferenciaSalida extends DocumentoBase {

	@NotNull
	private long documentoTransferenciaId;

	private long documentoReservaId;
	
	private boolean reposicion;

	@NotNull
	private String bodegaOrigen;

	@NotNull
	private String bodegaDestino;

	private String mensajeError;

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

	public void actualizarLineaDetalle(DocumentoDetalle detalle) {
		if (CollectionUtils.isNotEmpty(getDetalle())) {
			Optional<DocumentoDetalle> detalleopt = getDetalle().stream().filter(d -> d.getId() == detalle.getId())
					.findFirst();
			if (detalleopt.isPresent()) {
				DocumentoDetalle docDetalle = detalleopt.get();				
				docDetalle.setSaldo(detalle.getCantidad());
				docDetalle.setCantidad(detalle.getCantidad());
				
				if(CollectionUtils.isNotEmpty(detalle.getCompartimientos())) {
					docDetalle.getCompartimientos().clear();
					detalle.getCompartimientos().forEach(d -> docDetalle.agregarCompartimiento(d));
				}
				
			} else {
				agregarLineaDetalle(detalle);
			}
		} else {
			agregarLineaDetalle(detalle);
		}
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

	public long getDocumentoReservaId() {
		return documentoReservaId;
	}

	public long getDocumentoTransferenciaId() {
		return documentoTransferenciaId;
	}

	public String getEntregaMercaderia() {
		return entregaMercaderia;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public boolean isReposicion() {
		return reposicion;
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

	public void setDocumentoReservaId(long documentoReservaId) {
		this.documentoReservaId = documentoReservaId;
	}

	public void setDocumentoTransferenciaId(long documentoTransferenciaId) {
		this.documentoTransferenciaId = documentoTransferenciaId;
	}

	public void setEntregaMercaderia(String entregaMercaderia) {
		this.entregaMercaderia = entregaMercaderia;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public void setReposicion(boolean reposicion) {
		this.reposicion = reposicion;
	}
}
