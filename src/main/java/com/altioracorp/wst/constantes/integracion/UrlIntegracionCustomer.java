package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionCustomer {
	
	OBTENER_POR_CUSTOMER_NUMBER("/GpCustomer/GetCustomer/"),
	OBTENER_POR_CRITERIO_TODOS("/GpCustomer/GetCustomersByCriteria/"),
	OBTENER_POR_CRITERIO_ACTIVOS("/GpCustomer/GetCustomersActiveByCriteria/"),
	INTEGRAR_CUSTOMER("/GpCustomer/IntegrateCustomerTransaction");
	
	private String url;

	private UrlIntegracionCustomer(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
