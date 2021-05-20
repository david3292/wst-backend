package com.altioracorp.wst.xml.factura;

public enum CodigoImpuesto {

	ICE("3"),
	IRBPNR("5"),
	IVA("2");
	
	private String numero;

	private CodigoImpuesto(String numero) {
		this.numero = numero;
	}

	public String getNumero() {
		return numero;
	}
}
