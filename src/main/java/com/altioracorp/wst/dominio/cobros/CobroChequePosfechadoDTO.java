package com.altioracorp.wst.dominio.cobros;

public class CobroChequePosfechadoDTO {

	private long chequePosfechadoId;
	
	private String numeroCobro;
	
	private String numeroCobroFormaPagoGP;
	
	private String numeroCheque;
	
	private CobroFormaPagoEstado estado;
	
	public CobroChequePosfechadoDTO() {	}

	public CobroChequePosfechadoDTO(long chequePosfechadoId, String numeroCobro,
			String numeroCobroFormaPagoGP, String numeroCheque, CobroFormaPagoEstado estado) {
		this.chequePosfechadoId = chequePosfechadoId;
		this.numeroCobro = numeroCobro;
		this.numeroCobroFormaPagoGP = numeroCobroFormaPagoGP;
		this.numeroCheque = numeroCheque;
		this.estado = estado;
	}


	public long getChequePosfechadoId() {
		return chequePosfechadoId;
	}

	public String getNumeroCobro() {
		return numeroCobro;
	}

	public String getNumeroCobroFormaPagoGP() {
		return numeroCobroFormaPagoGP;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public CobroFormaPagoEstado getEstado() {
		return estado;
	}
	
}
