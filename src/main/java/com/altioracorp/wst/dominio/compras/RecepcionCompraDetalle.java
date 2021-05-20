package com.altioracorp.wst.dominio.compras;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;

@SuppressWarnings("serial")
@Entity
public class RecepcionCompraDetalle extends EntidadBase {

	private String codigoArticulo;
	
	private BigDecimal cantidadCompra;
	
	private BigDecimal cantidadRecepcion;
	
	@Column(name = "recepcion_compra_id", insertable = false, updatable = false)
	private long recepcionCompraId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cotizacion_detalle_id", referencedColumnName = "id", insertable = true, updatable = true, nullable = false)
	private CotizacionDetalle cotizacionDetalle;
	
	@Column
	private Integer numeroSecuencial;
	
	@Transient
	private BigDecimal saldo;

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public BigDecimal getCantidadCompra() {
		return cantidadCompra;
	}

	public void setCantidadCompra(BigDecimal cantidadCompra) {
		this.cantidadCompra = cantidadCompra;
	}

	public BigDecimal getCantidadRecepcion() {
		return cantidadRecepcion;
	}

	public void setCantidadRecepcion(BigDecimal cantidadRecepcion) {
		this.cantidadRecepcion = cantidadRecepcion;
	}

	public CotizacionDetalle getCotizacionDetalle() {
		return cotizacionDetalle;
	}

	public void setCotizacionDetalle(CotizacionDetalle cotizacionDetalle) {
		this.cotizacionDetalle = cotizacionDetalle;
	}

	public long getRecepcionCompraId() {
		return recepcionCompraId;
	}

	public void setRecepcionCompraId(long recepcionCompraId) {
		this.recepcionCompraId = recepcionCompraId;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public Integer getNumeroSecuencial() {
		return numeroSecuencial;
	}

	public void setNumeroSecuencial(Integer numeroSecuencial) {
		this.numeroSecuencial = numeroSecuencial;
	}
		
}
