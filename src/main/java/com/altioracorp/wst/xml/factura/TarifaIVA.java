package com.altioracorp.wst.xml.factura;

public enum TarifaIVA {

	CATORCE_PORCIENTO("3", "14"),
	CERO_PORCIENTO("0", "0"),
	DOCE_PORCIENTO("2", "12"),
	EXENTO_IVA("7", "_"),
	NO_OBJETO_IMPUESTO("6", "_");
	
	private String numero;
	
	private String porcentaje;

	private TarifaIVA(String numero, String porcentaje) {
		this.numero = numero;
		this.porcentaje = porcentaje;
	}

	public String getPorcentaje() {
		return porcentaje;
	}

	public String getNumero() {
		return numero;
	}
}
