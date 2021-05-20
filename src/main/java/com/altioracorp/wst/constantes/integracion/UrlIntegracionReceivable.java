package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionReceivable {

	INTEGRAR_APLICACION("/GpReceivable/IntegrateApply/"), 
	INTEGRAR_COBRO("/GpReceivable/IntegrateCashReceipt/");

	private String url;

	private UrlIntegracionReceivable(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
}
