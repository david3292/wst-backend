package com.altioracorp.wst.dominio.ventas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.altioracorp.wst.dominio.compras.OrdenCompraEstado;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class OrdenCompraDTO {

	private long id;
	
	private String codigoProveedor;

	private String nombreProveedor;
	
	private String numero;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaEmision; 
	
	private String bodegaEntrega;
	
	private String numeroRecepcion;
	
	private String numeroCotizacion;
	
	private String mensajeError;
	
	@Enumerated(EnumType.STRING)
	private OrdenCompraEstado estado;
	
	private BigDecimal total;
	
	private String nombreVendedor;
	
	private String condicionPago;
	
	private String condicionPagoGp;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNumeroRecepcion() {
		return numeroRecepcion;
	}

	public void setNumeroRecepcion(String numeroRecepcion) {
		this.numeroRecepcion = numeroRecepcion;
	}

	public OrdenCompraEstado getEstado() {
		return estado;
	}

	public void setEstado(OrdenCompraEstado estado) {
		this.estado = estado;
	}

	public String getNumeroCotizacion() {
		return numeroCotizacion;
	}

	public void setNumeroCotizacion(String numeroCotizacion) {
		this.numeroCotizacion = numeroCotizacion;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getBodegaEntrega() {
		return bodegaEntrega;
	}

	public void setBodegaEntrega(String bodegaEntrega) {
		this.bodegaEntrega = bodegaEntrega;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getNombreVendedor() {
		return nombreVendedor;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}

	public String getCodigoProveedor() {
		return codigoProveedor;
	}

	public void setCodigoProveedor(String codigoProveedor) {
		this.codigoProveedor = codigoProveedor;
	}

	public String getCondicionPago() {
		return condicionPago;
	}

	public void setCondicionPago(String condicionPago) {
		this.condicionPago = condicionPago;
	}

	public String getCondicionPagoGp() {
		return condicionPagoGp;
	}

	public void setCondicionPagoGp(String condicionPagoGp) {
		this.condicionPagoGp = condicionPagoGp;
	}
		
}
