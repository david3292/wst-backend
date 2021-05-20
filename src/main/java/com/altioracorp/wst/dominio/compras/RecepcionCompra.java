package com.altioracorp.wst.dominio.compras;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
public class RecepcionCompra extends EntidadBase{
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaRecepcion;
	
	private String numeroRecepcion;
	
	@Enumerated(EnumType.STRING)
	private RecepcionCompraEstado estado;
	
	private String mensajeError;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private OrdenCompra ordenCompra;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "recepcion_compra_id", nullable = false)
	private List<RecepcionCompraDetalle> detalle = new ArrayList<>();

	public LocalDateTime getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	
	public String getNumeroRecepcion() {
		return numeroRecepcion;
	}

	public void setNumeroRecepcion(String numeroRecepcion) {
		this.numeroRecepcion = numeroRecepcion;
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

	public OrdenCompra getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(OrdenCompra ordenCompra) {
		this.ordenCompra = ordenCompra;
	}

	public List<RecepcionCompraDetalle> getDetalle() {
		return detalle;
	}

	public void setDetalle(List<RecepcionCompraDetalle> detalle) {
		this.detalle = detalle;
	}
	
	public void actualizarDetalle(RecepcionCompraDetalle recepcionCompraDetalle) {
		if(this.detalle == null)
			this.detalle = new ArrayList<RecepcionCompraDetalle>();
		
		Optional<RecepcionCompraDetalle> detalleOpt = this.detalle.stream().filter(f -> f.getCodigoArticulo().equals(recepcionCompraDetalle.getCodigoArticulo())).findFirst();
		if(detalleOpt.isPresent()) {
			RecepcionCompraDetalle detalle = detalleOpt.get();
			detalle.setCantidadCompra(recepcionCompraDetalle.getCantidadCompra());
			detalle.setCantidadRecepcion(recepcionCompraDetalle.getCantidadRecepcion());
		}else {
			this.detalle.add(recepcionCompraDetalle);
		}
	}
}
