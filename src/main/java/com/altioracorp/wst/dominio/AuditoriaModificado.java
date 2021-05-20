package com.altioracorp.wst.dominio;

import java.util.Date;

public class AuditoriaModificado {
	
	private Date fecha;
	private String por;

	public AuditoriaModificado(String por, Date fecha) {
		this.por = por;
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getPor() {
		return por;
	}
}
