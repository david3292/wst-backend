package com.altioracorp.wst.dominio.logistica.dto;

import java.math.BigDecimal;

public class ArticuloReposicionDTO {

	private String codigoArticulo;

	private String descripcionArticulo;

	private String codigoAlternoArticulo;

	private String unidadMedida;

	private BigDecimal peso;

	private BigDecimal cantidadDisponible;

	private BigDecimal cantidadReserva;

	private BigDecimal cantidadProceso;

	private BigDecimal cantidadMinima;

	private BigDecimal cantidadMaxima;

	private BigDecimal cantidadTotal;

	public BigDecimal getCantidadDisponible() {
		return cantidadDisponible;
	}

	public BigDecimal getCantidadMaxima() {
		return cantidadMaxima;
	}

	public BigDecimal getCantidadMinima() {
		return cantidadMinima;
	}

	public BigDecimal getCantidadProceso() {
		return cantidadProceso;
	}

	public BigDecimal getCantidadReserva() {
		return cantidadReserva;
	}

	public BigDecimal getCantidadTotal() {
		return cantidadTotal;
	}

	public String getCodigoAlternoArticulo() {
		return codigoAlternoArticulo;
	}

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public ArticuloReposicionDTO() {
		super();
	}

	public ArticuloReposicionDTO(String codigoArticulo, String descripcionArticulo, String codigoAlternoArticulo,
			String unidadMedida, BigDecimal peso, BigDecimal cantidadDisponible, BigDecimal cantidadReserva,
			BigDecimal cantidadMinima, BigDecimal cantidadMaxima) {
		super();
		this.codigoArticulo = codigoArticulo;
		this.descripcionArticulo = descripcionArticulo;
		this.codigoAlternoArticulo = codigoAlternoArticulo;
		this.unidadMedida = unidadMedida;
		this.peso = peso;
		this.cantidadDisponible = cantidadDisponible;
		this.cantidadReserva = cantidadReserva;
		this.cantidadMinima = cantidadMinima;
		this.cantidadMaxima = cantidadMaxima;
		this.cantidadProceso = BigDecimal.ZERO;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setCantidadDisponible(BigDecimal cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	public void setCantidadMaxima(BigDecimal cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}

	public void setCantidadMinima(BigDecimal cantidadMinima) {
		this.cantidadMinima = cantidadMinima;
	}

	public void setCantidadProceso(BigDecimal cantidadProceso) {
		this.cantidadProceso = cantidadProceso;
	}

	public void setCantidadReserva(BigDecimal cantidadReserva) {
		this.cantidadReserva = cantidadReserva;
	}

	public void setCantidadTotal(BigDecimal cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public void setCodigoAlternoArticulo(String codigoAlternoArticulo) {
		this.codigoAlternoArticulo = codigoAlternoArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

}
