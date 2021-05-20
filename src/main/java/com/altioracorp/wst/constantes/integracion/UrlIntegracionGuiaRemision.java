package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionGuiaRemision {

	INTEGRAR_GUIA_REMISION("/GpGuiaRemision/GuiaRemisionIntegrate"),
	OBTENER_NUMERO_GP("/GpGuiaRemision/GetGrNumber/${locnCode}");
	
	private String url;

	private UrlIntegracionGuiaRemision(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
