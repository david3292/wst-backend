package com.altioracorp.wst.dominio.logistica.dto;

import java.time.LocalDateTime;

import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TransferenciaConsulta {

	private String numeroDocumento;
	
	private String numeroCotizacion;
	
	private String numeroReferencia;
	
	private TipoDocumento tipoTransferencia;
	
	private DocumentoEstado estado;
	
	private Perfil perfil;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaInicio;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaFin;
	
	private String bodegaTransaccion;
	
	private long usuarioId;
	
	private long puntoVentaId;
	
	private String bodega;

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public TipoDocumento getTipoTransferencia() {
		return tipoTransferencia;
	}

	public void setTipoTransferencia(TipoDocumento tipoTransferencia) {
		this.tipoTransferencia = tipoTransferencia;
	}

	public DocumentoEstado getEstado() {
		return estado;
	}

	public void setEstado(DocumentoEstado estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getBodegaTransaccion() {
		return bodegaTransaccion;
	}

	public void setBodegaTransaccion(String bodegaTransaccion) {
		this.bodegaTransaccion = bodegaTransaccion;
	}

	public long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public long getPuntoVentaId() {
		return puntoVentaId;
	}

	public void setPuntoVentaId(long puntoVentaId) {
		this.puntoVentaId = puntoVentaId;
	}

	public String getBodega() {
		return bodega;
	}

	public void setBodega(String bodega) {
		this.bodega = bodega;
	}

	public String getNumeroCotizacion() {
		return numeroCotizacion;
	}

	public void setNumeroCotizacion(String numeroCotizacion) {
		this.numeroCotizacion = numeroCotizacion;
	}

	public String getNumeroReferencia() {
		return numeroReferencia;
	}

	public void setNumeroReferencia(String numeroReferencia) {
		this.numeroReferencia = numeroReferencia;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
		
}
