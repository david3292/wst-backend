package com.altioracorp.wst.dominio.logistica.dto;

import java.math.BigDecimal;

public class ReposicionRespuestaCambioLineaDTO{

	private boolean error = Boolean.FALSE;

	private BigDecimal nuevaCantidadDisponible;
	
	private BigDecimal cantidadReponerOriginal;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public BigDecimal getNuevaCantidadDisponible() {
		return nuevaCantidadDisponible;
	}

	public void setNuevaCantidadDisponible(BigDecimal nuevaCantidadDisponible) {
		this.nuevaCantidadDisponible = nuevaCantidadDisponible;
	}

	public BigDecimal getCantidadReponerOriginal() {
		return cantidadReponerOriginal;
	}

	public void setCantidadReponerOriginal(BigDecimal cantidadReponerOriginal) {
		this.cantidadReponerOriginal = cantidadReponerOriginal;
	}
	
}
