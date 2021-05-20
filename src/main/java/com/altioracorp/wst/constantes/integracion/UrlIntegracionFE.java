package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionFE {
	
	CONSULTAR_DOCUMENTO_FE("/GpFE/GetDocumentFE");
	
	private String url;

	private UrlIntegracionFE(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
