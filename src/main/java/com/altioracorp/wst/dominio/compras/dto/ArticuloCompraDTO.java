package com.altioracorp.wst.dominio.compras.dto;

import java.math.BigDecimal;

import com.altioracorp.gpintegrator.client.Vendor.Vendor;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;

public class ArticuloCompraDTO {
	
	private Vendor proveedor;
	
	private Vendor proveedorCambio;
	
	private String condicionPagoGp;
	
	private String condicionPago;

	private long cotizacionId;
	
	private CotizacionDetalle cotizacionDetalle;
	
	private String bodegaEntrega;
	
	private BigDecimal cantidad;
	
	private BigDecimal costoUnitarioCompra;
	
	private BigDecimal precioVenta;
	
	private BigDecimal margenUtilidadOriginal;
	
	private BigDecimal margenUtilidad;
	
	private PuntoVenta puntoVenta;
		
	public ArticuloCompraDTO() { }
	
	public ArticuloCompraDTO(CotizacionDetalle cotizacionDetalle) {
		this.cotizacionDetalle = cotizacionDetalle;
		this.cantidad = this.cotizacionDetalle.getCantidad();
		this.costoUnitarioCompra = this.cotizacionDetalle.getCostoUnitario();
		this.precioVenta = this.cotizacionDetalle.getPrecioUnitario();
	}
	
	public Vendor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Vendor proveedor) {
		this.proveedor = proveedor;
	}
	
	public Vendor getProveedorCambio() {
		return proveedorCambio;
	}

	public void setProveedorCambio(Vendor proveedorCambio) {
		this.proveedorCambio = proveedorCambio;
	}

	public long getCotizacionId() {
		return cotizacionId;
	}

	public void setCotizacionId(long cotizacionId) {
		this.cotizacionId = cotizacionId;
	}

	public CotizacionDetalle getCotizacionDetalle() {
		return cotizacionDetalle;
	}

	public void setCotizacionDetalle(CotizacionDetalle cotizacionDetalle) {
		this.cotizacionDetalle = cotizacionDetalle;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getCostoUnitarioCompra() {
		return costoUnitarioCompra;
	}

	public void setCostoUnitarioCompra(BigDecimal costoUnitarioCompra) {
		this.costoUnitarioCompra = costoUnitarioCompra;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public String getBodegaEntrega() {
		return bodegaEntrega;
	}

	public void setBodegaEntrega(String bodegaEntrega) {
		this.bodegaEntrega = bodegaEntrega;
	}

	public BigDecimal getMargenUtilidad() {
		return margenUtilidad;
	}

	public void setMargenUtilidad(BigDecimal margenUtilidad) {
		this.margenUtilidad = margenUtilidad;
	}

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
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

	public BigDecimal getMargenUtilidadOriginal() {
		return margenUtilidadOriginal;
	}

	public void setMargenUtilidadOriginal(BigDecimal margenUtilidadOriginal) {
		this.margenUtilidadOriginal = margenUtilidadOriginal;
	}
		
}
