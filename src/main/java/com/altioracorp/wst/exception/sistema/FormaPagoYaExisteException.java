package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class FormaPagoYaExisteException extends SistemaException{

	private String formaPago;
	
	public FormaPagoYaExisteException(String fpago) {
		formaPago = fpago;
	}
		
	public String getFormaPago() {
		return formaPago;
	}

	@Override
	public String getMessage() {
		return String.format("Forma de Pago %s ya existente", formaPago);
	}

}
