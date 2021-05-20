package com.altioracorp.wst.dominio.logistica.dto;

public class TransferenciaResumenDTO {
	
	private String tranferenciaCreada;
	
	private String errorTransferencia;
	
	private String guiaRemisionCreada;
	
	private String errorGuiaRemision;
	
	private Object objeto;

	public String getTranferenciaCreada() {
		return tranferenciaCreada;
	}

	public void setTranferenciaCreada(String tranferenciaCreada) {
		this.tranferenciaCreada = tranferenciaCreada;
	}

	public String getErrorTransferencia() {
		return errorTransferencia;
	}

	public void setErrorTransferencia(String errorTransferencia) {
		this.errorTransferencia = errorTransferencia;
	}

	public String getGuiaRemisionCreada() {
		return guiaRemisionCreada;
	}

	public void setGuiaRemisionCreada(String guiaRemisionCreada) {
		this.guiaRemisionCreada = guiaRemisionCreada;
	}

	public String getErrorGuiaRemision() {
		return errorGuiaRemision;
	}

	public void setErrorGuiaRemision(String errorGuiaRemision) {
		this.errorGuiaRemision = errorGuiaRemision;
	}

	public Object getObjeto() {
		return objeto;
	}

	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}
	
}
