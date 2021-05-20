package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionPaymentTerms {
	
	INTEGRAR("/GpPaymentTerms/registerTerms");
	
	private String url;

	private UrlIntegracionPaymentTerms(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
