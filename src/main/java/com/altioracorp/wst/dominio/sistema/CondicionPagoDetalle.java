package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class CondicionPagoDetalle extends EntidadBase {

	@NotNull
	private Integer numeroCuota;

	@NotNull
	private Double porcentaje;

	@NotNull
	private Integer numeroDias;

	public CondicionPagoDetalle() {
	}

	public CondicionPagoDetalle(Integer numeroCuota, Double porcentaje, Integer numeroDias) {
		this.numeroCuota = numeroCuota;
		this.porcentaje = porcentaje;
		this.numeroDias = numeroDias;
	}

	public Integer getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public Integer getNumeroDias() {
		return numeroDias;
	}

	public void setNumeroDias(Integer numeroDias) {
		this.numeroDias = numeroDias;
	}

	@Override
	public String toString() {
		return "CondicionPagoDetalle [numeroCuota=" + numeroCuota + ", porcentaje=" + porcentaje + ", numeroDias="
				+ numeroDias + "]";
	}

}
