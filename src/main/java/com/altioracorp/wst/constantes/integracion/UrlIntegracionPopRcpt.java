package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionPopRcpt {
	
	INTEGRAR_POP_RCPT("/GpPoDocument/IntegratePopReceiptTransaction"),
	OBTENER_NUMERO_GP("/GpPoDocument/GetPoPReceiptDocumentNumber");
	
	private String url;
	
	private UrlIntegracionPopRcpt(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
