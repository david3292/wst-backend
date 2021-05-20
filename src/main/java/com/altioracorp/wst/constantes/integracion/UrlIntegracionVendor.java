package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionVendor {
	
	OBTENER_POR_CRITERIO("/GpVendor/GetVendorsByCriteria"),
	OBTENER_POR_ID("/GpVendor/GetVendorsById"),
	CONDICIONES_PAGO("/GpVendor/PaymentConditions");
	
	private String url;

	private UrlIntegracionVendor(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
