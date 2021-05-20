package com.altioracorp.wst.controlador.sistema;

import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Secuencial;

public class SecuencialDTO {

	private long id;

	private TipoDocumentoDTO tipoDocumento;

	private PuntoVenta puntoVenta;

	private String docIdGP;

	private String abreviatura;

	private int susecion;
	
	private boolean activo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TipoDocumentoDTO getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoDTO tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public String getDocIdGP() {
		return docIdGP;
	}

	public void setDocIdGP(String docIdGP) {
		this.docIdGP = docIdGP;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public int getSusecion() {
		return susecion;
	}

	public void setSusecion(int susecion) {
		this.susecion = susecion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public SecuencialDTO() {
	}
	
	public SecuencialDTO(Secuencial secuencial) {
		this.abreviatura = secuencial.getAbreviatura();
		this.docIdGP = secuencial.getDocIdGP();
		this.susecion = secuencial.getSucesion();
		this.tipoDocumento = new TipoDocumentoDTO(secuencial.getTipoDocumento());
		this.puntoVenta = secuencial.getPuntoVenta();
		this.activo = secuencial.isActivo();
	}

}
