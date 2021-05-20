package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionPo {

	INTEGRAR_PO("/GpPoDocument/IntegratePoTransaction"),
	OBTENER_NUMERO_GP("/GpPoDocument/GetPoDocumentNumber"),
	CERRAR_ORDEN_COMPRA("/GpPoDocument/ClosePO/${poNumber}")
	;
	
	private String url;
	
	private UrlIntegracionPo(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
