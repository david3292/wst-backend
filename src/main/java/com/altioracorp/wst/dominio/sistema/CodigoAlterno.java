package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class CodigoAlterno extends EntidadBase{

	private String codigo_alterno;
	
	@NotNull
	private boolean enUso;

	public String getCodigo_alterno() {
		return codigo_alterno;
	}

	public void setCodigo_alterno(String codigo_alterno) {
		this.codigo_alterno = codigo_alterno;
	}

	public boolean isEnUso() {
		return enUso;
	}

	public void setEnUso(boolean enUso) {
		this.enUso = enUso;
	}
	
}
