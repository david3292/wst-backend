package com.altioracorp.wst.dominio.compras;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;

@SuppressWarnings("serial")
@Entity
public class OrdenCompraDetalle extends EntidadBase {
	
	private String codigoArticulo;

	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal cantidad;
	
	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal saldo;

	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal costoUnitario;
	
	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal margenUtilidadOriginal;
	
	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal margenUtilidad;

	// Crear método para cálculo
	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal precioVenta;
	
	
	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal total;
	
	@Column
	private Integer numeroSecuencia;

	@Column(name = "orden_compra_id", insertable = false, updatable = false)
	private long ordenCompraId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cotizacion_detalle_id", referencedColumnName = "id", insertable = true, updatable = true, nullable = false)
	public CotizacionDetalle cotizacionDetalle;
	
	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public BigDecimal getCostoUnitario() {
		return costoUnitario;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public void setCostoUnitario(BigDecimal costoUnitario) {
		this.costoUnitario = costoUnitario;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public long getOrdenCompraId() {
		return ordenCompraId;
	}

	public void setOrdenCompraId(long ordenCompraId) {
		this.ordenCompraId = ordenCompraId;
	}

	public CotizacionDetalle getCotizacionDetalle() {
		return cotizacionDetalle;
	}

	public void setCotizacionDetalle(CotizacionDetalle cotizacionDetalle) {
		this.cotizacionDetalle = cotizacionDetalle;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void calcularTotal() {
		this.total = this.cantidad.multiply(this.costoUnitario).setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getMargenUtilidad() {
		return margenUtilidad;
	}

	public void setMargenUtilidad(BigDecimal margenUtilidad) {
		this.margenUtilidad = margenUtilidad;
	}
	
	public BigDecimal getMargenUtilidadOriginal() {
		return margenUtilidadOriginal;
	}

	public void setMargenUtilidadOriginal(BigDecimal margenUtilidadOriginal) {
		this.margenUtilidadOriginal = margenUtilidadOriginal;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public Integer getNumeroSecuencia() {
		return numeroSecuencia;
	}

	public void setNumeroSecuencia(Integer numeroSecuencia) {
		this.numeroSecuencia = numeroSecuencia;
	}

	public boolean esCantidadCompraMayorACotizacion() {
		return this.cantidad.compareTo(this.cotizacionDetalle.getCantidad()) > 0;
	}
}
