package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;

public class ArticuloCompartimientoDTO {

	private String codigoArticulo;
	
	private String codigoBodega;
	
	private String compartimiento;
	
	private BigDecimal cantidadExistente;

	private BigDecimal cantidadReservada;

	private BigDecimal cantidadTotal;

	public ArticuloCompartimientoDTO() {
		
	}

	public ArticuloCompartimientoDTO(String codigoArticulo, String codigoBodega, String compartimiento,
			BigDecimal cantidadReservada, BigDecimal cantidadExistente) {
		this.codigoArticulo = codigoArticulo;
		this.codigoBodega = codigoBodega;
		this.compartimiento = compartimiento;
		this.cantidadReservada = cantidadReservada;
		this.cantidadExistente = cantidadExistente;
		this.cantidadTotal = cantidadExistente.subtract(cantidadReservada);
	}

	public BigDecimal getCantidadExistente() {
		return cantidadExistente;
	}

	public BigDecimal getCantidadReservada() {
		return cantidadReservada;
	}

	public BigDecimal getCantidadTotal() {
		return cantidadTotal;
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

	public void setCantidadExistente(BigDecimal cantidadExistente) {
		this.cantidadExistente = cantidadExistente;
	}

	public void setCantidadReservada(BigDecimal cantidadReservada) {
		this.cantidadReservada = cantidadReservada;
	}

	public void setCantidadTotal(BigDecimal cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
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
	
}
