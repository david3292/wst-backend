package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegradorConductor {

	LISTAR_CONDUSTORES("/GpConductor/GetConductores");
	
	private String url;

	private UrlIntegradorConductor(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
