package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionSiteSetup {	
	
	LISTAR_TODOS("/GpSiteSetup/GetSitesSetup"),
	LISTAR_POR_LOCNCODE("/GpSiteSetup/GetByLocnCode/${locnCode}"),
	SITES_FOR_CREATE_UPDATE_ITEM("/GpSiteSetup/GetSitesForCreateUpdateItem"),
	;
	
	private String url;

	private UrlIntegracionSiteSetup(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
