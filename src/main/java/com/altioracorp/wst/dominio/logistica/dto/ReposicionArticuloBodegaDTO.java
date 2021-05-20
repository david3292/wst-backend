package com.altioracorp.wst.dominio.logistica.dto;

import java.math.BigDecimal;

public class ReposicionArticuloBodegaDTO {

	private String codigoArticulo;

	private String codigoBodega;

	private boolean bodegaOrigen;

	private BigDecimal cantidadReponer;
	

	public ReposicionArticuloBodegaDTO() {	}

	public ReposicionArticuloBodegaDTO(String codigoArticulo, String codigoBodega, boolean bodegaOrigen,
			BigDecimal cantidadReponer) {
		this.codigoArticulo = codigoArticulo;
		this.codigoBodega = codigoBodega;
		this.bodegaOrigen = bodegaOrigen;
		this.cantidadReponer = cantidadReponer;
	}

	public BigDecimal getCantidadReponer() {
		return cantidadReponer;
	}

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public String getCodigoBodega() {
		return codigoBodega;
	}

	public boolean isBodegaOrigen() {
		return bodegaOrigen;
	}

	public void setBodegaOrigen(boolean bodegaOrigen) {
		this.bodegaOrigen = bodegaOrigen;
	}

	public void setCantidadReponer(BigDecimal cantidadReponer) {
		this.cantidadReponer = cantidadReponer;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public void setCodigoBodega(String codigoBodega) {
		this.codigoBodega = codigoBodega;
	}

}
