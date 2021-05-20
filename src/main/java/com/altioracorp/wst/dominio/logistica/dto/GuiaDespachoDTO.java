package com.altioracorp.wst.dominio.logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.Entrega;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class GuiaDespachoDTO {

	private long idGuiaDespacho;
	
	private String identificacionCliente;
	
	private String nombreCliente;
	
	private String numeroFactura;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaFactura;
	
	private String numeroGuiaDespacho;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaGuiaDespacho;
	
	private BigDecimal valorTotalFactura;
	
	private  DocumentoEstado estadoGuiaRemision;
	
	private Entrega entrega;
	
	private BigDecimal pesoGuiaDespacho;

	public GuiaDespachoDTO() {
		super();
	}

	public GuiaDespachoDTO(long idGuiaDespacho, String numeroGuiaDespacho, DocumentoEstado estadoGuiaRemision) {
		super();
		this.idGuiaDespacho = idGuiaDespacho;
		this.numeroGuiaDespacho = numeroGuiaDespacho;
		this.estadoGuiaRemision = estadoGuiaRemision;
	}

	public long getIdGuiaDespacho() {
		return idGuiaDespacho;
	}

	public void setIdGuiaDespacho(long idGuiaDespacho) {
		this.idGuiaDespacho = idGuiaDespacho;
	}

	public String getIdentificacionCliente() {
		return identificacionCliente;
	}

	public void setIdentificacionCliente(String identificacionCliente) {
		this.identificacionCliente = identificacionCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public LocalDateTime getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(LocalDateTime fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public String getNumeroGuiaDespacho() {
		return numeroGuiaDespacho;
	}

	public void setNumeroGuiaDespacho(String numeroGuiaDespacho) {
		this.numeroGuiaDespacho = numeroGuiaDespacho;
	}

	public LocalDateTime getFechaGuiaDespacho() {
		return fechaGuiaDespacho;
	}

	public void setFechaGuiaDespacho(LocalDateTime fechaGuiaDespacho) {
		this.fechaGuiaDespacho = fechaGuiaDespacho;
	}

	public BigDecimal getValorTotalFactura() {
		return valorTotalFactura;
	}

	public void setValorTotalFactura(BigDecimal valorTotalFactura) {
		this.valorTotalFactura = valorTotalFactura;
	}

	public DocumentoEstado getEstadoGuiaRemision() {
		return estadoGuiaRemision;
	}

	public void setEstadoGuiaRemision(DocumentoEstado estadoGuiaRemision) {
		this.estadoGuiaRemision = estadoGuiaRemision;
	}

	public Entrega getEntrega() {
		return entrega;
	}

	public void setEntrega(Entrega entrega) {
		this.entrega = entrega;
	}

	public BigDecimal getPesoGuiaDespacho() {
		return pesoGuiaDespacho;
	}

	public void setPesoGuiaDespacho(BigDecimal pesoGuiaDespacho) {
		this.pesoGuiaDespacho = pesoGuiaDespacho;
	}
	
}
