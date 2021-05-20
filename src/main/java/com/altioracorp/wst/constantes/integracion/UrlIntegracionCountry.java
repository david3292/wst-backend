package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionCountry {
	
	LISTAR_PAISES_TODO("/GpCommons/GetCountriesAll");
	
	private String url;

	private UrlIntegracionCountry(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
