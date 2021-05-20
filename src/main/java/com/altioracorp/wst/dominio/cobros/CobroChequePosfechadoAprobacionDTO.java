package com.altioracorp.wst.dominio.cobros;

import com.altioracorp.wst.dominio.RevisionEstado;

public class CobroChequePosfechadoAprobacionDTO {

	private RevisionEstado estado;

	private String tipo;

	private ChequePosfechadoDTO dto;

	public CobroChequePosfechadoAprobacionDTO() {
	}

	public CobroChequePosfechadoAprobacionDTO(RevisionEstado estado, ChequePosfechadoDTO dto) {
		this.dto = dto;
		this.estado = estado;
		determinarTipo();
	}

	public RevisionEstado getEstado() {
		return estado;
	}
	
	private void determinarTipo() {
		if (dto.isCanje())
			tipo = "CANJE";
		if (dto.getDiasProrroga() > 0)
			tipo = "PRORROGA";
	}

	public String getTipo() {
		return tipo;
	}

	public ChequePosfechadoDTO getDto() {
		return dto;
	}

}
