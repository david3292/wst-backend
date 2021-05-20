package com.altioracorp.wst.exception.cobros;

@SuppressWarnings("serial")
public class FormaPagoNoTieneAplicacionesException extends CobrosException {

	private String formaPago;
	private String numeroDocumento;

	public FormaPagoNoTieneAplicacionesException(String formaPago, String numeroDocumento) {
		super();
		this.formaPago = formaPago;
		this.numeroDocumento = numeroDocumento;
	}

	@Override
	public String getMessage() {
		return String.format("La forma de pago %s %s debe tener por lo menos una aplicación.", formaPago,
				numeroDocumento);
	}

}
