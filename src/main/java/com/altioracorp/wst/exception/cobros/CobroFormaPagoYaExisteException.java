package com.altioracorp.wst.exception.cobros;

@SuppressWarnings("serial")
public class CobroFormaPagoYaExisteException extends CobrosException{

	private String formaPago;
	
	private String numeroFactura;
	
	public CobroFormaPagoYaExisteException(String formaPago, String numeroFactura) {
		this.formaPago = formaPago;
		this.numeroFactura = numeroFactura;
	}


	@Override
	public String getMessage() {
		return String.format("Para el Documento: %s ya está registrado la forma de pago %s ", numeroFactura, formaPago);
	}
	
	
}
