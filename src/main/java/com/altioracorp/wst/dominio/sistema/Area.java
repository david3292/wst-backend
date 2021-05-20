package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class Area extends EntidadBase {

	@NotNull
	private String codigo;
	
	@NotNull
	private String areaFuncional;
	
	private String areaReporta;
	
	@NotNull
	private boolean activo;

	public Area() {	}
	
	public Area(String codigo, String areaFuncional, String areaReporta) {
		this.codigo = codigo;
		this.areaFuncional = areaFuncional;
		this.areaReporta = areaReporta;
		this.activo = Boolean.TRUE;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getAreaFuncional() {
		return areaFuncional;
	}

	public void setAreaFuncional(String areaFuncional) {
		this.areaFuncional = areaFuncional;
	}

	public String getAreaReporta() {
		return areaReporta;
	}

	public void setAreaReporta(String areaReporta) {
		this.areaReporta = areaReporta;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Override
	public String toString() {
		return "Area [codigo=" + codigo + ", areaFuncional=" + areaFuncional + ", areaReporta=" + areaReporta
				+ ", activo=" + activo + "]";
	}
	
}
