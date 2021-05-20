package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class ConfiguracionSistema extends EntidadBase {

	@Enumerated(EnumType.STRING)
	@NotNull
	private ConfigSistema nombre;
	
	@NotNull
	@Column(columnDefinition = "numeric(19,5)")
	private double valor;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private ConfigSistemaUnidad unidadMedida;
	
	@NotNull
	private boolean activo;
	
	public ConfiguracionSistema() {}

	public ConfiguracionSistema(ConfigSistema nombre, double valor, ConfigSistemaUnidad unidadMedida) {
		this.nombre = nombre;
		this.valor = valor;
		this.unidadMedida = unidadMedida;
		this.activo = Boolean.TRUE;
	}


	public ConfigSistema getNombre() {
		return nombre;
	}

	public void setNombre(ConfigSistema nombre) {
		this.nombre = nombre;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public ConfigSistemaUnidad getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(ConfigSistemaUnidad unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Override
	public String toString() {
		return "ConfiguracionSistema [nombre=" + nombre + ", valor=" + valor + ", unidadMedida=" + unidadMedida
				+ ", activo=" + activo + "]";
	};	
	
	
}
