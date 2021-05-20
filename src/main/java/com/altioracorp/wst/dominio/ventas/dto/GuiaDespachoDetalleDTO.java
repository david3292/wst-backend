package com.altioracorp.wst.dominio.ventas.dto;

import java.math.BigDecimal;
import java.util.List;

import com.altioracorp.wst.dominio.ventas.DocumentoDetalleCompartimiento;

public class GuiaDespachoDetalleDTO {

	private String descripcionArticulo;
	
	private String codigoArticulo;
	
	private String codigoArticuloAlterno;
	
	private BigDecimal cantidad;
	
	private BigDecimal saldo;
	
	private String bodega;
	
	private List<DocumentoDetalleCompartimiento> compartimientos;
	
	public GuiaDespachoDetalleDTO() {}

	public GuiaDespachoDetalleDTO(String descripcionArticulo, String codigoArticulo, String codigoArticuloAlterno, BigDecimal cantidad, BigDecimal saldo, String bodega,
			List<DocumentoDetalleCompartimiento> compartimientos) {
		this.descripcionArticulo = descripcionArticulo;
		this.codigoArticulo = codigoArticulo;
		this.codigoArticuloAlterno = codigoArticuloAlterno;
		this.cantidad = cantidad;
		this.saldo = saldo;
		this.bodega = bodega;
		this.compartimientos = compartimientos;
	}

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getBodega() {
		return bodega;
	}

	public void setBodega(String bodega) {
		this.bodega = bodega;
	}

	public List<DocumentoDetalleCompartimiento> getCompartimientos() {
		return compartimientos;
	}

	public void setCompartimientos(List<DocumentoDetalleCompartimiento> compartimientos) {
		this.compartimientos = compartimientos;
	}

	public String getCodigoArticuloAlterno() {
		return codigoArticuloAlterno;
	}

	public void setCodigoArticuloAlterno(String codigoArticuloAlterno) {
		this.codigoArticuloAlterno = codigoArticuloAlterno;
	}
	
}
