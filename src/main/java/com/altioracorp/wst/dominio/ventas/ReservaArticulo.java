package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class ReservaArticulo extends EntidadBase {
	
	private String codigoArticulo;
	
	private String codigoBodega;

	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal cantidad;

	public ReservaArticulo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReservaArticulo(String codigoArticulo, String codigoBodega, BigDecimal cantidad) {

		this.codigoArticulo = codigoArticulo;
		this.codigoBodega = codigoBodega;
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

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	
	public void setCodigoBodega(String codigoBodega) {
		this.codigoBodega = codigoBodega;
	}
	
	@Override
	public String toString() {
		return "ReservaArticulo [codigoArticulo=" + codigoArticulo + ", codigoBodega=" + codigoBodega
			 + ", cantidad=" + cantidad + "]";
	}

}
