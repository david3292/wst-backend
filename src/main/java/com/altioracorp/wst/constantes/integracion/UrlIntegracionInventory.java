package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionInventory {
	
	INTEGRAR_INVENTORY("/GpInventory/IntegrateInventory"),
	OBTENER_NUMERO_GP("/GpInventory/GetIvNumber/${ivDocType}");
	
	private String url;
	
	private UrlIntegracionInventory(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
}
