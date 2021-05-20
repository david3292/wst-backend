package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionCompany {
	
	OBTENER_EMPRESA_INFORMACION("/GpCommons/GetCompanyInformation"),
	OBTENER_INFORACION_TRIBUTARIA("/GpCommons/GetInformacionTributaria"),
	OBTENER_COMPANY_LOCATION("/GpCommons/GetInformacionCompania")
	;
	
	private String url;

	private UrlIntegracionCompany(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
