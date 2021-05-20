package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionSop {

	OBTENER_NUMERO_DOCUMENTO("/GpDocument/GetSopNumber/"),
	INTEGRAR_TRNASACCION_SOP("/GpDocument/IntegrateSopTransaction/"),
	INSERTAR_LOCALIZACION_SOP("/GpDocument/InsertLocationSopDocument/"),
	CONSULTAR_DOCUMENTO("/GpDocument/GetDocument/"),
	CONSULTAR_DOCUMENTO_NC("/GpDocument/GetCreditNoteDocuments/"),
	CONSULTAR_DOCUMENTO_COBRO("/GpDocument/GetPaymentDocuments/"),
	CONSULTAR_DOCUMENTO_CONTABILIZADO("/GpDocument/GetDocumentPosted/");
	
	private String url;

	private UrlIntegracionSop(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
