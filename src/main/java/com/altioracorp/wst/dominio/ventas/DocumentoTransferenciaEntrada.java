package com.altioracorp.wst.dominio.ventas;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;

@SuppressWarnings("serial")
@Entity
public class DocumentoTransferenciaEntrada extends DocumentoBase {

	@NotNull
	private long documentoTransferenciaSalidaId;
	
	private long documentoReservaId;
	
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
	
	@Transient
	private String guiaRemision;
	
	public void actualizarLineaDetalle(DocumentoDetalle detalle) {
		if(CollectionUtils.isNotEmpty(getDetalle())) {
			Optional<DocumentoDetalle> detalleopt = getDetalle().stream().filter(d -> d.getId() == detalle.getId()).findFirst();
			if(detalleopt.isPresent()) {
				DocumentoDetalle docDetalle = detalleopt.get();
				docDetalle.setSaldo(detalle.getCantidad());
				docDetalle.setCantidad(detalle.getCantidad());
				docDetalle.getCompartimientos().clear();
				
				if(CollectionUtils.isNotEmpty(detalle.getCompartimientos()))
					detalle.getCompartimientos().forEach(d -> docDetalle.agregarCompartimiento(d));
			}else {
				agregarLineaDetalle(detalle);
			}
		}else {
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

	public long getDocumentoTransferenciaSalidaId() {
		return documentoTransferenciaSalidaId;
	}

	public String getEntregaMercaderia() {
		return entregaMercaderia;
	}

	public String getGuiaRemision() {
		return guiaRemision;
	}

	public String getMensajeError() {
		return mensajeError;
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

	public void setDocumentoTransferenciaSalidaId(long documentoTransferenciaSalidaId) {
		this.documentoTransferenciaSalidaId = documentoTransferenciaSalidaId;
	}
	
	public void setEntregaMercaderia(String entregaMercaderia) {
		this.entregaMercaderia = entregaMercaderia;
	}

	public void setGuiaRemision(String guiaRemision) {
		this.guiaRemision = guiaRemision;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
}
