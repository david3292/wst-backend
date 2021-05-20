package com.altioracorp.wst.dominio.cobros;

import java.util.List;

import com.altioracorp.wst.dominio.sistema.PerfilNombre;

public class SolicitarReporteChequePosfechadoDTO {

	private List<Long> chequesPosfechadoIds;
	
	private PerfilNombre perfil;

	public List<Long> getChequesPosfechadoIds() {
		return chequesPosfechadoIds;
	}

	public void setChequesPosfechadoIds(List<Long> chequesPosfechadoIds) {
		this.chequesPosfechadoIds = chequesPosfechadoIds;
	}

	public PerfilNombre getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilNombre perfil) {
		this.perfil = perfil;
	}
	
	
}
