package com.altioracorp.wst.exception.cobros;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class FormaPagoValorTotalNoAplicadoException extends CobrosException {

	private String formaPago;
	private String numeroDocumento;
	private BigDecimal saldo;

	public FormaPagoValorTotalNoAplicadoException(String formaPago, String numeroDocumento, BigDecimal saldo) {
		super();
		this.formaPago = formaPago;
		this.numeroDocumento = numeroDocumento;
		this.saldo = saldo;
	}

	@Override
	public String getMessage() {
		return String.format("El valor de %s %s debe aplicarse en su totalidad. Saldo $%s .", formaPago,
				numeroDocumento, saldo);
	}

}
