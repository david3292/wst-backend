package com.altioracorp.wst.dominio.ventas;

import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;

public class CriterioArticuloDTO {

	private String criterio;
	
	private PuntoVenta puntoVenta;
	
	private Perfil perfil;

	public CriterioArticuloDTO() {
		super();
	}

	public CriterioArticuloDTO(String criterio) {
		super();
		this.criterio = criterio;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
}
