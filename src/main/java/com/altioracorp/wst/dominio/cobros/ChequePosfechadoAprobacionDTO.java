package com.altioracorp.wst.dominio.cobros;

import java.util.List;

import com.altioracorp.wst.dominio.RevisionEstado;

public class ChequePosfechadoAprobacionDTO {

	private List<ChequePosfechadoDTO> chequesAProcesar;
	
	private RevisionEstado estado;
	
	private String observacion;
	
	public ChequePosfechadoAprobacionDTO() {	}

	public List<ChequePosfechadoDTO> getChequesAProcesar() {
		return chequesAProcesar;
	}

	public void setChequesAProcesar(List<ChequePosfechadoDTO> chequesAProcesar) {
		this.chequesAProcesar = chequesAProcesar;
	}

	public RevisionEstado getEstado() {
		return estado;
	}

	public void setEstado(RevisionEstado estado) {
		this.estado = estado;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}	
	
}
