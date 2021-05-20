package com.altioracorp.wst.xml.factura;

import java.util.stream.Stream;

public enum FormaPagoXML {

	DESCONOCIDO("DESCONOCIDO", "OTRA FORMA DE PAGO"),
	FP01("01", "SIN UTILIZACIÓN DE SISTEMA FINANCIERO"),
	FP02("02", "CHEQUE PROPIO"),
	FP03("03", "CHEQUE CERTIFICADO"),
	FP04("04", "CHEQUE DE GERENCIA"),
	FP05("05", "CHEQUE DEL EXTERIOR"),
	FP06("06", "DÉBITO DE CUENTA"),
	FP07("07", "TRANSFERENCIA PROPIO BANCO"),
	FP08("08", "TRANSFERENCIA OTRO BANCO NACIONAL"),
	FP09("09", "TRANSFERENCIA BANCO EXTERIOR"),
	FP10("10", "TARJETA DE CRÉDITO NACIONAL"),
	FP11("11", "TARJETA DE CRÉDITO INTERNACIONAL"),
	FP12("12", "GIRO"),
	FP13("13", "DEPOSITO EN CUENTA"),
	FP14("14", "ENDOSO DE INVERSIÓN"),
	FP15("15", "COMPENSACIÓN DE DEUDAS"),
	FP16("16", "TARJETA DE DÉBITO"),
	FP17("17", "DINERO ELECTRÓNICO"),
	FP18("18", "TARJETA PREPAGO"),
	FP19("19", "TARJETA DE CRÉDITO"),
	FP20("20", "OTROS CON UTILIZACIÓN DE SIST. FINANC."),
	FP21("21", "ENDOSO DE TÍTULOS");
	
	public static final FormaPagoXML paraCodigo(String codigo) {
		return Stream.of(FormaPagoXML.values())
				.filter((formaPago) -> formaPago.getCodigo().equals(codigo))
				.findAny()
				.orElse(FormaPagoXML.DESCONOCIDO);
	}
	
	private String codigo;

	private String descripcion;

	private FormaPagoXML(String codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
}
