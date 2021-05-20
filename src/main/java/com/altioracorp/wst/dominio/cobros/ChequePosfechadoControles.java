package com.altioracorp.wst.dominio.cobros;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.RevisionEstado;

@SuppressWarnings("serial")
@Entity
public class ChequePosfechadoControles extends EntidadBase {

	@ManyToOne(fetch = FetchType.EAGER)
	private ChequePosfechado cheque;

	@Enumerated(EnumType.STRING)
	private RevisionEstado estado;

	private boolean fechaDiferente;

	private boolean canje;

	private int diasProrroga;

	private String observacion;

	public ChequePosfechadoControles() {	}

	public ChequePosfechadoControles(ChequePosfechado cheque, boolean fechaDiferente,
			boolean canje, int diasProrroga, String observacion) {
		super();
		this.cheque = cheque;
		this.fechaDiferente = fechaDiferente;
		this.canje = canje;
		this.diasProrroga = diasProrroga;
		this.observacion = observacion;
	}

	public ChequePosfechado getCheque() {
		return cheque;
	}

	public int getDiasProrroga() {
		return diasProrroga;
	}

	public RevisionEstado getEstado() {
		return estado;
	}

	public String getObservacion() {
		return observacion;
	}



	public boolean isCanje() {
		return canje;
	}

	public boolean isFechaDiferente() {
		return fechaDiferente;
	}

	public void setCanje(boolean canje) {
		this.canje = canje;
	}



	public void setCheque(ChequePosfechado cheque) {
		this.cheque = cheque;
	}

	public void setDiasProrroga(int diasProrroga) {
		this.diasProrroga = diasProrroga;
	}

	public void setEstado(RevisionEstado estado) {
		this.estado = estado;
	}

	public void setFechaDiferente(boolean fechaDiferente) {
		this.fechaDiferente = fechaDiferente;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Override
	public String toString() {
		return "ChequePosfechadoControles [cheque=" + cheque + ", estado=" + estado + ", fechaDiferente="
				+ fechaDiferente + ", canje=" + canje + ", diasProrroga=" + diasProrroga + ", observacion="
				+ observacion + "]";
	}

}
