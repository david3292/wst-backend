package com.altioracorp.wst.dominio.logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.TipoTransferencia;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TransferenciaDTO {

	private long id;
	
	private String creadoPor;
	
	private DocumentoEstado estado;

	private TipoDocumento tipo;

	private String numero;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaVencimiento;

	private String bodegaOrigen;

	private String bodegaDestino;

	private String cotizacionNumero;
	
	private String cotizacionEstado;

	private String cotizacionCreadoPor;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime cotizacionFechaEmision;

	private String nombreusuarioVendedor;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaEmision;

	private BigDecimal pesoTransferencia;
	
	private TipoTransferencia tipoTransferencia;
	
	private String numeroTransferenciaPadre;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	public DocumentoEstado getEstado() {
		return estado;
	}

	public void setEstado(DocumentoEstado estado) {
		this.estado = estado;
	}

	public TipoDocumento getTipo() {
		return tipo;
	}

	public void setTipo(TipoDocumento tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public LocalDateTime getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
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

	public String getCotizacionNumero() {
		return cotizacionNumero;
	}

	public void setCotizacionNumero(String cotizacionNumero) {
		this.cotizacionNumero = cotizacionNumero;
	}

	public String getCotizacionEstado() {
		return cotizacionEstado;
	}

	public void setCotizacionEstado(String cotizacionEstado) {
		this.cotizacionEstado = cotizacionEstado;
	}

	public String getCotizacionCreadoPor() {
		return cotizacionCreadoPor;
	}

	public void setCotizacionCreadoPor(String cotizacionCreadoPor) {
		this.cotizacionCreadoPor = cotizacionCreadoPor;
	}

	public String getNombreusuarioVendedor() {
		return nombreusuarioVendedor;
	}

	public void setNombreusuarioVendedor(String nombreusuarioVendedor) {
		this.nombreusuarioVendedor = nombreusuarioVendedor;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public LocalDateTime getCotizacionFechaEmision() {
		return cotizacionFechaEmision;
	}

	public void setCotizacionFechaEmision(LocalDateTime cotizacionFechaEmision) {
		this.cotizacionFechaEmision = cotizacionFechaEmision;
	}

	public BigDecimal getPesoTransferencia() {
		return pesoTransferencia;
	}

	public void setPesoTransferencia(BigDecimal pesoTransferencia) {
		this.pesoTransferencia = pesoTransferencia;
	}

	public TipoTransferencia getTipoTransferencia() {
		return tipoTransferencia;
	}

	public void setTipoTransferencia(TipoTransferencia tipoTransferencia) {
		this.tipoTransferencia = tipoTransferencia;
	}

	public String getNumeroTransferenciaPadre() {
		return numeroTransferenciaPadre;
	}

	public void setNumeroTransferenciaPadre(String numeroTransferenciaPadre) {
		this.numeroTransferenciaPadre = numeroTransferenciaPadre;
	}
	
}
