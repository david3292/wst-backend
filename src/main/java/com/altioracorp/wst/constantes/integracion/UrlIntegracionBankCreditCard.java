package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionBankCreditCard {
	
	LISTAR_BANCOS("/GpBank/GetBanks"),
	LISTAR_TARJETAS_CREDITO("/GpCreditCard/GetCreditCards");
	
	private String url;

	private UrlIntegracionBankCreditCard(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
