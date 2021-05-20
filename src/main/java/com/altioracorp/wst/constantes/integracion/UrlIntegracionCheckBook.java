package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionCheckBook {
	
	LISTAR_TODOS("/GpCheckBook/GetCheckBooks");
	
	private String url;

	private UrlIntegracionCheckBook(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
