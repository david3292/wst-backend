package com.altioracorp.wst.dominio.ventas;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class MotivoNotaCredito extends EntidadBase {

	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoDevolucion tipoDevolucion;

	private String motivo;

	private boolean cambioRazonSocial = false;
	
	private boolean activo;

	public String getMotivo() {
		return motivo;
	}

	public TipoDevolucion getTipoDevolucion() {
		return tipoDevolucion;
	}

	public boolean isCambioRazonSocial() {
		return cambioRazonSocial;
	}

	public void setCambioRazonSocial(boolean cambioRazonSocial) {
		this.cambioRazonSocial = cambioRazonSocial;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public void setTipoDevolucion(TipoDevolucion tipoDevolucion) {
		this.tipoDevolucion = tipoDevolucion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Override
	public String toString() {
		return "MotivoNotaCredito [tipoDevolucion=" + tipoDevolucion + ", motivo=" + motivo + ", cambioRazonSocial="
				+ cambioRazonSocial + "]";
	}

}
