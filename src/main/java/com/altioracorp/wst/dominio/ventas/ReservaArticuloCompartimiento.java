package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class ReservaArticuloCompartimiento extends EntidadBase {

	private String codigoArticulo;

	private String codigoBodega;

	private String compartimiento;

	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal cantidad;

	public ReservaArticuloCompartimiento() {

	}

	public ReservaArticuloCompartimiento(String codigoArticulo, String codigoBodega, String compartimiento) {
		
		this.codigoArticulo = codigoArticulo;
		this.codigoBodega = codigoBodega;
		this.compartimiento = compartimiento;
		this.cantidad = BigDecimal.ZERO;
	}

	public ReservaArticuloCompartimiento(String codigoArticulo, String codigoBodega, String compartimiento,
			BigDecimal cantidad) {
		
		this.codigoArticulo = codigoArticulo;
		this.codigoBodega = codigoBodega;
		this.compartimiento = compartimiento;
		this.cantidad = cantidad;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public String getCodigoBodega() {
		return codigoBodega;
	}

	public String getCompartimiento() {
		return compartimiento;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public void setCodigoBodega(String codigoBodega) {
		this.codigoBodega = codigoBodega;
	}

	public void setCompartimiento(String compartimiento) {
		this.compartimiento = compartimiento;
	}

	@Override
	public String toString() {
		return "ReservaArticuloCompartimiento [codigoArticulo=" + codigoArticulo + ", codigoBodega=" + codigoBodega
				+ ", cantidad=" + cantidad + ", compartimiento=" + compartimiento + "]";
	}

}
