package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class FormaPago extends EntidadBase {

	@NotNull
	@Enumerated(EnumType.STRING)
	private FormaPagoNombre nombre;
	
	@NotNull
	private boolean integracionCobro;
	
	@NotNull
	private boolean chequePosFechado;
	
	@NotNull
	private boolean activo;
	
	public FormaPago() {}

	public FormaPagoNombre getNombre() {
		return nombre;
	}
	
	public void setNombre(FormaPagoNombre nombre) {
		this.nombre = nombre;
	}
	
	public boolean isIntegracionCobro() {
		return integracionCobro;
	}

	public void setIntegracionCobro(boolean integracionCobro) {
		this.integracionCobro = integracionCobro;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	public boolean isChequePosFechado() {
		return chequePosFechado;
	}

	public void setChequePosFechado(boolean chequePosFechado) {
		this.chequePosFechado = chequePosFechado;
	}

	@Override
	public String toString() {
		return "FormaPago [nombre=" + nombre + ", integracionCobro=" + integracionCobro + ", chequePosFechado="
				+ chequePosFechado + ", activo=" + activo + "]";
	}
	
}
