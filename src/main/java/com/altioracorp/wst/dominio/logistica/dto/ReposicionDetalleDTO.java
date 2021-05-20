package com.altioracorp.wst.dominio.logistica.dto;

import java.math.BigDecimal;

public class ReposicionDetalleDTO {

	private long idDetalle;
	
	private String codigoArticulo;
	
	private String descripcionArticulo;
	
	private String codigoAlternoArticulo;
	
	private String unidadMedida;
	
	private String bodegaOrigen;
	
	private String bodegaDestino;
	
	private BigDecimal disponibleOrigen;
	
	private BigDecimal disponibleDestino;
	
	private BigDecimal cantidadReponer;
	
	private BigDecimal cantidadSugerida;
	
	private BigDecimal peso;
	
	private String compartimientoOrigen;
	
	public ReposicionDetalleDTO() {	}

	public ReposicionDetalleDTO(String codigoArticulo, String descripcionArticulo, String codigoAlternoArticulo,
			String unidadMedida, String bodegaOrigen, String bodegaDestino, BigDecimal disponibleOrigen,
			BigDecimal disponibleDestino, BigDecimal cantidadReponer, BigDecimal peso, String compartimientoOrigen) {
		this.codigoArticulo = codigoArticulo;
		this.descripcionArticulo = descripcionArticulo;
		this.codigoAlternoArticulo = codigoAlternoArticulo;
		this.unidadMedida = unidadMedida;
		this.bodegaOrigen = bodegaOrigen;
		this.bodegaDestino = bodegaDestino;
		this.disponibleOrigen = disponibleOrigen;
		this.disponibleDestino = disponibleDestino;
		this.cantidadReponer = cantidadReponer;
		this.peso = peso;
		this.compartimientoOrigen = compartimientoOrigen;
		this.cantidadSugerida = cantidadReponer;
	}

	public String getBodegaDestino() {
		return bodegaDestino;
	}

	public String getBodegaOrigen() {
		return bodegaOrigen;
	}
	
	public BigDecimal getCantidadReponer() {
		return cantidadReponer;
	}
	
	public String getCodigoAlternoArticulo() {
		return codigoAlternoArticulo;
	}

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public String getCompartimientoOrigen() {
		return compartimientoOrigen;
	}

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	public BigDecimal getDisponibleDestino() {
		return disponibleDestino;
	}

	public BigDecimal getDisponibleOrigen() {
		return disponibleOrigen;
	}

	public long getIdDetalle() {
		return idDetalle;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setIdDetalle(long idDetalle) {
		this.idDetalle = idDetalle;
	}

	public BigDecimal getCantidadSugerida() {
		return cantidadSugerida;
	}
	
}
