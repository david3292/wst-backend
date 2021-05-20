package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class DocumentoDetalleCompartimiento extends EntidadBase {

	private BigDecimal cantidad;

	private String compartimiento;

	public DocumentoDetalleCompartimiento() {
		super();
	}

	public DocumentoDetalleCompartimiento(BigDecimal cantidad, String compartimiento) {
		super();
		this.cantidad = cantidad;
		this.compartimiento = compartimiento;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public String getCompartimiento() {
		return compartimiento;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public void setCompartimiento(String compartimiento) {
		this.compartimiento = compartimiento;
	}

	@Override
	public String toString() {
		return "DocumentoDetalleCompartimiento [compartimiento=" + compartimiento + ", cantidad=" + cantidad + "]";
	}

}
