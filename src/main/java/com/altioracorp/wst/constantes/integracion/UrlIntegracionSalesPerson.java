package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionSalesPerson {
	
	LISTAR_PERSONA_VENTAS("/GpSalesPerson/GetAllSalesPersons");
	
	private String url;

	private UrlIntegracionSalesPerson(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
